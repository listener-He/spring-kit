package org.springframework.web.servlet.mvc.method.annotation;

import cn.hutool.core.io.IoUtil;
import org.hehh.cloud.spring.mvc.annotation.Param;
import org.hehh.cloud.spring.mvc.http.ContentCachingRequestWrapper;
import org.hehh.cloud.spring.mvc.http.RequestNameValueHttpInputMessage;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: HeHui
 * @date: 2020-04-04 15:20
 * @description: 参数解析器 {@link Param 支持body格式}
 */
public class ParamJsonArgumentResolver extends RequestResponseBodyMethodProcessor {


    private ConfigurableBeanFactory beanFactory;

    private  BeanExpressionContext expressionContext;



    /**
     *  请求参数名绑定缓存
     */
    private final Map<MethodParameter, ParamJsonArgumentResolver.NamedValueInfo> namedValueInfoCache = new ConcurrentHashMap<>(256);


    /**
     *  请求参数缓存
     */
    private final Map<NativeWebRequest, String> valueInfoCache = new ConcurrentHashMap<>(256);



    /**
     * Basic constructor with converters only. Suitable for resolving
     * {@code @RequestBody}. For handling {@code @ResponseBody} consider also
     * providing a {@code ContentNegotiationManager}.
     *
     * @param converters
     */
    public ParamJsonArgumentResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    /**
     * Basic constructor with converters and {@code ContentNegotiationManager}.
     * Suitable for resolving {@code @RequestBody} and handling
     * {@code @ResponseBody} without {@code Request~} or
     * {@code ResponseBodyAdvice}.
     *
     * @param converters
     * @param manager
     */
    public ParamJsonArgumentResolver(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager) {
        super(converters, manager);
    }




    /**
     * Complete constructor for resolving {@code @RequestBody} method arguments.
     * For handling {@code @ResponseBody} consider also providing a
     * {@code ContentNegotiationManager}.
     *
     * @param converters
     * @param requestResponseBodyAdvice
     * @param beanFactory
     * @since 4.2
     */
    public ParamJsonArgumentResolver(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice, ConfigurableBeanFactory beanFactory) {
        super(converters, requestResponseBodyAdvice);
        this.beanFactory = beanFactory;
        this.expressionContext =
                (beanFactory != null ? new BeanExpressionContext(beanFactory, new RequestScope()) : null);
    }




    /**
     * Complete constructor for resolving {@code @RequestBody} and handling
     * {@code @ResponseBody}.
     *
     * @param converters
     * @param manager
     * @param requestResponseBodyAdvice
     */
    public ParamJsonArgumentResolver(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, manager, requestResponseBodyAdvice);
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Param.class);
    }




    /**
     * Throws MethodArgumentNotValidException if validation fails.
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @throws HttpMessageNotReadableException if {@link RequestBody#required()}
     *                                         is {@code true} and there is no body content or if there is no suitable
     *                                         converter to read the content with.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        parameter = parameter.nestedIfOptional();





        Object arg = null;
        String  name = null;

        String body = getBody(webRequest);

        try {
            NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
            name = namedValueInfo.name;
            if(!StringUtils.hasText(body)){
                 body = namedValueInfo.defaultValue;
                if(!StringUtils.hasText(body)){
                    body = "{}";
                }
            }
             arg = readBodyMessageConverters(webRequest,body, parameter, parameter.getNestedGenericParameterType());
             resolveStringValue(name);
        }catch(Exception e){
              arg = super.readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
              name = Conventions.getVariableNameForParameter(parameter);
        }






//        if (binderFactory != null) {
//            WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
//            if (arg != null) {
//                validateIfApplicable(binder, parameter);
//                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
//                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
//                }
//            }
//            if (mavContainer != null) {
//                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
//            }
//        }

        return adaptArgumentIfNecessary(arg, parameter);
    }


    /**
     *  获取body
     * @param request
     * @return
     * @throws IOException
     */
    protected String getBody(NativeWebRequest request) throws IOException {
//        String s = valueInfoCache.get(request);
//        if(!StringUtils.hasText(s)){
          ContentCachingRequestWrapper servletRequest = request.getNativeRequest(ContentCachingRequestWrapper.class);
          if(null == servletRequest){
              return null;
          }
            String  s = IoUtil.read(new ByteArrayInputStream(servletRequest.getBody()), StandardCharsets.UTF_8);
//            if(!StringUtils.hasText(s)){
//                s = "{}";
//            }
//            valueInfoCache.put(request,s);
//        }

        return s;
    }



    protected <T> Object readBodyMessageConverters(NativeWebRequest webRequest,String body, MethodParameter parameter, Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");

        ServletServerHttpRequest inputMessage = null;


        try {
            inputMessage = new RequestNameValueHttpInputMessage(servletRequest,body,parameter.getParameterName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpMessageNotReadableException("构建RequestNameValueHttpInputMessage异常: " +
                    parameter.getExecutable().toGenericString(),e, inputMessage);
        }



        Object arg = readWithMessageConverters(inputMessage, parameter, paramType);
        if (arg == null && checkRequired(parameter)) {
            throw new HttpMessageNotReadableException("Required request body is missing: " +
                    parameter.getExecutable().toGenericString(), inputMessage);
        }
        return arg;
    }

    /**
     * Obtain the named value for the given method parameter.
     */
    private NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
        NamedValueInfo namedValueInfo = this.namedValueInfoCache.get(parameter);
        if (namedValueInfo == null) {
            namedValueInfo = createNamedValueInfo(parameter);
            namedValueInfo = updateNamedValueInfo(parameter, namedValueInfo);
            this.namedValueInfoCache.put(parameter, namedValueInfo);
        }
        return namedValueInfo;
    }




    @Override
    protected boolean checkRequired(MethodParameter parameter) {
        Param requestBody = parameter.getParameterAnnotation(Param.class);
        return (requestBody != null && requestBody.required() && !parameter.isOptional());
    }



    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        Param ann = parameter.getParameterAnnotation(Param.class);
        return (ann != null ? new NamedValueInfo(ann) : new NamedValueInfo());
    }


    /**
     * Create a new NamedValueInfo based on the given NamedValueInfo with sanitized values.
     */
    protected NamedValueInfo updateNamedValueInfo(MethodParameter parameter, NamedValueInfo info) {
        String name = info.name;
        if (info.name.isEmpty()) {
            name = parameter.getParameterName();
            if (name == null) {
                throw new IllegalArgumentException(
                        "Name for argument type [" + parameter.getNestedParameterType().getName() +
                                "] not available, and parameter name information not found in class file either.");
            }
        }
        String defaultValue = (ValueConstants.DEFAULT_NONE.equals(info.defaultValue) ? null : info.defaultValue);
        return new NamedValueInfo(name, info.required, defaultValue);
    }


    /**
     * Resolve the given annotation-specified value,
     * potentially containing placeholders and expressions.
     */
    @Nullable
    private Object resolveStringValue(String value) {
        if (this.beanFactory == null) {
            return value;
        }
        String placeholdersResolved = this.beanFactory.resolveEmbeddedValue(value);
        BeanExpressionResolver exprResolver = this.beanFactory.getBeanExpressionResolver();
        if (exprResolver == null || this.expressionContext == null) {
            return value;
        }
        return exprResolver.evaluate(placeholdersResolved, this.expressionContext);
    }




    /**
     * Represents the information about a named value, including name, whether it's required and a default value.
     */
    protected static class NamedValueInfo {

        private final String name;

        private final boolean required;

        @Nullable
        private final String defaultValue;

        public NamedValueInfo(String name, boolean required, @Nullable String defaultValue) {
            this.name = name;
            this.required = required;
            this.defaultValue = defaultValue;
        }

        public NamedValueInfo(){
            this("",true,ValueConstants.DEFAULT_NONE);
        }

        public NamedValueInfo(Param param){
            this(param.name(),param.required(),param.defaultValue());
        }
    }
}
