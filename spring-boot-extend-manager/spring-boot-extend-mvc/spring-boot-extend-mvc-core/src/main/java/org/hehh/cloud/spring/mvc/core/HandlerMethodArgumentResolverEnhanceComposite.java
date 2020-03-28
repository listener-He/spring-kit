package org.hehh.cloud.spring.mvc.core;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
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
            /**
             *  获取请求参数流
             */
            HttpInputMessage inputMessage = createInputMessage(webRequest);
            MediaType mediaType = inputMessage.getHeaders().getContentType();
            Class<?> aClass = parameter.getContainingClass();
            if(resolverAdapter.supportsParameter(parameter,mediaType)){
                webRequest = resolverAdapter.beforeResolver(parameter, webRequest, inputMessage, mediaType,aClass);
            }



        }


        return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
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
