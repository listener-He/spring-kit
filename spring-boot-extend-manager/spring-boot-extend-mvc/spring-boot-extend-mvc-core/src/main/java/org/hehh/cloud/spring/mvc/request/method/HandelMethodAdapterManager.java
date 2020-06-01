package org.hehh.cloud.spring.mvc.request.method;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: HeHui
 * @date: 2020-04-12 02:03
 * @description: 请求方法切面适配器管理
 */
public  class HandelMethodAdapterManager {


    /**
     *  自定义的切面
     */
    private final List<IHandlerMethodAdapter> customAdapter = new LinkedList<>();

    private final Map<HandlerMethod, IHandlerMethodAdapter> argumentResolverCache =
            new ConcurrentHashMap<>(256);


    /**
     * Add the given {@link IHandlerMethodAdapter}.
     */
    public HandelMethodAdapterManager addResolver(IHandlerMethodAdapter resolver) {
        this.customAdapter.add(resolver);
        return this;
    }

    /**
     * Add the given {@link IHandlerMethodAdapter IHandlerMethodAdapter}.
     * @since 4.3
     */
    public HandelMethodAdapterManager addResolvers(
            @Nullable IHandlerMethodAdapter... resolvers) {

        if (resolvers != null) {
            Collections.addAll(this.customAdapter, resolvers);
        }
        return this;
    }



    /**
     * Add the given {@link IHandlerMethodAdapter IHandlerMethodAdapter}.
     */
    public HandelMethodAdapterManager addResolvers(
            @Nullable List<? extends IHandlerMethodAdapter> resolvers) {

        if (resolvers != null) {
            this.customAdapter.addAll(resolvers);
        }
        return this;
    }



    /**
     * Clear the list of configured resolvers.
     * @since 4.3
     */
    public void clear() {
        this.customAdapter.clear();
    }


    /**
     *  处理请求
     * @param request
     * @param handlerMethod
     * @return
     */
    public HttpServletRequest resolve(HttpServletRequest request,HandlerMethod handlerMethod){

        /**
         *  解析请求头
         */
        String content_type = request.getHeader(HttpHeaders.CONTENT_TYPE);
        MediaType mediaType = (StringUtils.hasLength(content_type) ? MediaType.parseMediaType(content_type) : null);

        IHandlerMethodAdapter resolver = getResolver(handlerMethod, mediaType);
        if(resolver != null){
           return resolver.beforeResolver(request,handlerMethod);
        }

        return request;
    }



    /**
     * Find a registered {@link IHandlerMethodAdapter} that supports
     * the given method .
     */
    @Nullable
    private IHandlerMethodAdapter getResolver(HandlerMethod method, MediaType mediaType) {
        IHandlerMethodAdapter result = this.argumentResolverCache.get(method);
        if (result == null) {
            for (IHandlerMethodAdapter resolver : this.customAdapter) {
                if (resolver.supportsMethod(method,mediaType)) {
                    result = resolver;
                    this.argumentResolverCache.put(method, result);
                    break;
                }
            }
        }
        return result;
    }





}
