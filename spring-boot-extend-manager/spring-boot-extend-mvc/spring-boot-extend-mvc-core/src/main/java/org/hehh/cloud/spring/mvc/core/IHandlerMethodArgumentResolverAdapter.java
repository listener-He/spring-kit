package org.hehh.cloud.spring.mvc.core;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author: HeHui
 * @create: 2020-03-22 23:42
 * @description: 请求参数适配器
 **/
public interface IHandlerMethodArgumentResolverAdapter {


    /**
     *  判断是否支持参数适配
     * Whether the given {@linkplain MethodParameter method parameter} is
     * supported by this resolver.
     *  调用方{@link HandlerMethodArgumentResolverEnhanceComposite}
     * @param parameter the method parameter to check
     * @param mediaType  request  mediaType
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    boolean supportsParameter(MethodParameter parameter, MediaType mediaType);


    /**
     *  在参数解析之前调用,{@link HandlerMethodArgumentResolverEnhanceComposite}
     *    需要返回一个{@link NativeWebRequest} 如果request没变化可直接返回 webRequest,但如果发生变更了。请务必重新组装{@link NativeWebRequest}
     * @param parameter url绑定方法参数
     * @param webRequest 当前请求
     * @param mediaType 媒体类型
     * @param paramClass 参数类型
     * @return
     */
    NativeWebRequest beforeResolver(MethodParameter parameter, NativeWebRequest webRequest,MediaType mediaType,Class<?> paramClass);
}
