package org.hehh.cloud.spring.mvc.core;

import org.hehh.cloud.spring.mvc.annotation.Param;
import org.hehh.cloud.spring.mvc.annotation.Required;
import org.hehh.cloud.spring.mvc.http.CacheRequestHttpInputMessage;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves method parameters by delegating to a list of registered
 * {@link HandlerMethodArgumentResolver HandlerMethodArgumentResolvers}.
 * Previously resolved method parameters are cached for faster lookups.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author: HeHui
 * @create: 2020-03-22 18:29
 * @description: 增强处理程序方法参数解析器组合
 **/
public class HandlerMethodArgumentResolverEnhanceComposite  extends HandlerMethodArgumentResolverComposite {


    private final List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<>();


    private HandlerMethodArgumentResolverAdapterComposite resolverAdapter;


    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
            new ConcurrentHashMap<>(256);

//    private final Map<MethodParameter, Map<MediaType,HandlerMethodArgumentResolver>> argumentResolverCache =
//            new ConcurrentHashMap<>(256);


    public HandlerMethodArgumentResolverEnhanceComposite(){ }


    /**
     *  带请求参数适配器的构造器
     * @param resolverAdapters 请求参数适配器
     */
    public HandlerMethodArgumentResolverEnhanceComposite(List<IHandlerMethodArgumentResolverAdapter> resolverAdapters){
         if(!CollectionUtils.isEmpty(resolverAdapters)){
             Collections.unmodifiableList(resolverAdapters);
             this.resolverAdapter = new HandlerMethodArgumentResolverAdapterComposite(resolverAdapters);
         }
    }





    /**
     * Clear the list of configured resolvers.
     *
     * @since 4.3
     */
    @Override
    public void clear() {
        super.clear();
        this.argumentResolvers.clear();

    }


    /**
     * Whether the given {@linkplain MethodParameter method parameter} is
     * supported by any registered {@link HandlerMethodArgumentResolver}.
     *
     * @param parameter
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    /**
     * Iterate over registered
     * {@link HandlerMethodArgumentResolver HandlerMethodArgumentResolvers}
     * and invoke the one that supports it.
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @throws IllegalArgumentException if no suitable argument resolver is found
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {




        HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
        if (resolver == null) {
            throw new IllegalArgumentException("Unsupported parameter type [" +
                    parameter.getParameterType().getName() + "]. supportsParameter should be called first.");
        }

        if(resolverAdapter != null){

            String content_type = webRequest.getHeader(HttpHeaders.CONTENT_TYPE);
            MediaType mediaType = (StringUtils.hasLength(content_type) ? MediaType.parseMediaType(content_type) : MediaType.MULTIPART_FORM_DATA);

            Class<?> aClass = parameter.getContainingClass();
            if(resolverAdapter.supportsParameter(parameter,mediaType)){
                webRequest = resolverAdapter.beforeResolver(parameter, webRequest, mediaType,aClass);
            }

        }

        Object argument = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);


        /**
         *  如果是 {@link Param 注解的特殊处理}
         */
        if(parameter.hasParameterAnnotation(Param.class)){
            argument = required(parameter, argument);
            binderParam(parameter, mavContainer, webRequest, binderFactory, argument);
        }



        if(argument == null && parameter.hasParameterAnnotation(Required.class)){
            String msg = parameter.getParameterAnnotation(Required.class).value();
            if(StringUtils.hasText(msg)){
                throw new ServletRequestBindingException(msg);
            }
            throw new ServletRequestBindingException("Missing argument '" + parameter.getParameterName() +
                    "' for method parameter of type " + parameter.getNestedParameterType().getSimpleName());
        }

        return argument;
    }


    /**
     *  绑定参数
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @param argument
     * @throws Exception
     */
    private void binderParam(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory, Object argument) throws Exception {

            String name = parameter.getParameterName();
            if (binderFactory != null) {
                WebDataBinder binder = binderFactory.createBinder(webRequest, argument, name);
                if (argument != null) {
                    validateIfApplicable(binder, parameter);
                    if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                        throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                    }
                }
                if (mavContainer != null) {
                    mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
                }
            }
    }


    /**
     *  验证必填
     * @param parameter
     * @param argument
     * @return
     * @throws ServletRequestBindingException
     */
    private Object required(MethodParameter parameter, Object argument) throws ServletRequestBindingException {
        if(argument == null){
            Param parameterAnnotation = parameter.getParameterAnnotation(Param.class);
            if(StringUtils.hasText(parameterAnnotation.defaultValue())){
                argument = parameterAnnotation.defaultValue();
            }
            if(argument == null && parameterAnnotation.required()){
                throw new ServletRequestBindingException("Missing argument '" + parameter.getParameterName() +
                        "' for method parameter of type " + parameter.getNestedParameterType().getSimpleName());
            }
        }

        return argument;
    }


    /**
     * Return a read-only list with the contained resolvers, or an empty list.
     */
    @Override
    public List<HandlerMethodArgumentResolver> getResolvers() {

        /**
         *  新增代码
         */
        if(CollectionUtils.isEmpty(this.argumentResolvers)){
            /**
             *  调用父类实现
             */
            List<HandlerMethodArgumentResolver> resolverList = super.getResolvers();
            if(CollectionUtils.isEmpty(argumentResolvers)){
                /**
                 *  把值赋值给当前类
                 */
                resolverList.forEach(v-> argumentResolvers.add(v));
            }
        }
        return this.argumentResolvers;

    }



    /**
     * Find a registered {@link HandlerMethodArgumentResolver} that supports
     * the given method parameter.
     */
    @Nullable
    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
            HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
            if (result == null) {
                /**
                 *  此处解析器集合原是父类的 this.argumentResolverCache.
                 */
                for (HandlerMethodArgumentResolver resolver : getResolvers()) {
                    if (resolver.supportsParameter(parameter)) {
                        result = resolver;
                        this.argumentResolverCache.put(parameter, result);
                        break;
                    }
                }
            }
            return result;
    }


//    /**
//     * Find a registered {@link HandlerMethodArgumentResolver} that supports
//     * the given method parameter.
//     */
//    @Nullable
//    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter, MediaType mediaType) {
//        if(mediaType != null){
//            Map<MediaType,HandlerMethodArgumentResolver> result = this.argumentResolverCache.get(parameter);
//            if (result == null || result.get(mediaType) == null) {
//                /**
//                 *  此处解析器集合原是父类的 this.argumentResolverCache.
//                 */
//                for (HandlerMethodArgumentResolver resolver : getResolvers()) {
//                    if (resolver.supportsParameter(parameter)) {
////                        if(resolver instanceof RequestParamJsonArgumentResolver
////                                && !(null != mediaType  && mediaType.includes(MediaType.APPLICATION_JSON))){
////                            continue;
////                        }
//                        if(result == null){
//                            result = new ConcurrentHashMap(3);
//                        }
//                        result.put(mediaType,resolver);
//                        this.argumentResolverCache.put(parameter, result);
//                        break;
//                    }
//                }
//            }
//            return null == result ? null : result.get(mediaType);
//        }
//        return null;
//    }





    /**
     *  新增代码
     * Validate the binding target if applicable.
     * <p>The default implementation checks for {@code @javax.validation.Valid},
     * Spring's {@link org.springframework.validation.annotation.Validated},
     * and custom annotations whose name starts with "Valid".
     * @param binder the DataBinder to be used
     * @param parameter the method parameter descriptor
     * @since 4.1.5
     */
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
                binder.validate(validationHints);
                break;
            }
        }
    }


    /** 此处新增代码
     * Whether to raise a fatal bind exception on validation errors.
     * @param binder the data binder used to perform data binding
     * @param parameter the method parameter descriptor
     * @return {@code true} if the next method argument is not of type {@link Errors}
     * @since 4.1.5
     */
    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }



    /**
     *   此处新增代码
     * Create a new {@link HttpInputMessage} from the given {@link NativeWebRequest}.
     * @param webRequest the web request to create an input message from
     * @return the input message
     */
    protected HttpInputMessage createInputMessage(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        return new CacheRequestHttpInputMessage(servletRequest);
    }




}
