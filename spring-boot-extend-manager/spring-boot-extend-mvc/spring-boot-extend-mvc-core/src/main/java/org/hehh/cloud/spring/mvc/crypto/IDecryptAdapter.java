package org.hehh.cloud.spring.mvc.crypto;


import org.hehh.cloud.spring.decrypt.annotation.Decrypt;
import org.hehh.cloud.spring.mvc.core.HandlerMethodArgumentResolverEnhanceComposite;
import org.hehh.cloud.spring.mvc.core.IHandlerMethodArgumentResolverAdapter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author: HeHui
 * @create: 2020-03-22 17:14
 * @description: 解密适配器
 **/
public interface IDecryptAdapter extends IHandlerMethodArgumentResolverAdapter {


    /**
     *  是否需要有注解才解密
     * @return
     */
    boolean isScanAnnotation();


    /**
     *  是否支持解密
     * @param parameter
     * @return
     */
    boolean supportsDecrypt(MethodParameter parameter);


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
    default NativeWebRequest beforeResolver(MethodParameter parameter, NativeWebRequest webRequest, MediaType mediaType, Class<?> paramClass){
        NativeWebRequest request = decode(parameter,webRequest, mediaType, paramClass);
        if(request == null){
            return webRequest;
        }

        return request;
    }


    /**
     *  解密
     * @param parameter    url绑定方法参数
     * @param request 原始请求
     * @param mediaType    媒体类型
     * @param paramClass   参数类型
     * @return
     */
    NativeWebRequest decode(MethodParameter parameter, NativeWebRequest request,MediaType mediaType, Class<?> paramClass);






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
    default boolean supportsParameter(MethodParameter parameter, MediaType mediaType){
        boolean supportsDecrypt = supportsDecrypt(parameter);
        if (!isScanAnnotation() && supportsDecrypt) {
            return true;
        }


        /**
         *  判断class是否存在注解 or 方法上面有没有注解 or 参数上面有没有注释
         */
        if (supportsDecrypt && (parameter.hasParameterAnnotation(Decrypt.class)
                || parameter.hasMethodAnnotation(Decrypt.class)
                || parameter.getContainingClass().getAnnotation(Decrypt.class) != null)) {
            return true;
        }

        return false;
    }
}
