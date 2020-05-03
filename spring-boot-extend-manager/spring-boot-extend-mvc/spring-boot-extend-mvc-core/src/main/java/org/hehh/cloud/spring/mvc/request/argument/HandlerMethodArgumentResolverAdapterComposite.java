package org.hehh.cloud.spring.mvc.request.argument;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author: HeHui
 * @create: 2020-03-23 00:22
 * @description: 请求参数适配组合
 **/
public class HandlerMethodArgumentResolverAdapterComposite implements IHandlerMethodArgumentResolverAdapter {


    private final List<IHandlerMethodArgumentResolverAdapter> requestParamAdapters = new LinkedList<>();





    public HandlerMethodArgumentResolverAdapterComposite(List<IHandlerMethodArgumentResolverAdapter> handlerMethodArgumentResolverAdapters){
        if(!CollectionUtils.isEmpty(handlerMethodArgumentResolverAdapters)){

            handlerMethodArgumentResolverAdapters.forEach(v->requestParamAdapters.add(v));
        }
    }







    /**
     * 判断是否支持参数适配
     * Whether the given {@linkplain MethodParameter method parameter} is
     * supported by this resolver.
     * 调用方{@link HandlerMethodArgumentResolverEnhanceComposite}
     *
     * @param parameter the method parameter to check
     * @param mediaType request  mediaType
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter, MediaType mediaType) {
        return !CollectionUtils.isEmpty(requestParamAdapters);
    }





    /**
     * 在参数解析之前调用,{@link HandlerMethodArgumentResolverEnhanceComposite}
     * 需要返回一个{@link NativeWebRequest} 如果request没变化可直接返回 webRequest,但如果发生变更了。请务必重新组装{@link NativeWebRequest}
     *
     * @param parameter    url绑定方法参数
     * @param webRequest   当前请求
     * @param mediaType    媒体类型
     * @param paramClass   参数类型
     * @return
     */
    @Override
    public NativeWebRequest beforeResolver(MethodParameter parameter, NativeWebRequest webRequest,  MediaType mediaType, Class<?> paramClass) {
        if(supportsParameter(parameter,mediaType)){
            Optional<IHandlerMethodArgumentResolverAdapter> first = requestParamAdapters.stream().filter(v -> v.supportsParameter(parameter, mediaType)).findFirst();
            if(first.isPresent()){
               return first.get().beforeResolver(parameter,webRequest,mediaType,paramClass);
            }
        }
        return webRequest;
    }
}
