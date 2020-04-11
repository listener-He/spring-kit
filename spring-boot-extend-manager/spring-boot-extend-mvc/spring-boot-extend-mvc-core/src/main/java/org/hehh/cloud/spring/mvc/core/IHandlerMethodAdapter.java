package org.hehh.cloud.spring.mvc.core;

import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: HeHui
 * @date: 2020-04-12 01:57
 * @description: 请求方法适配器
 */
public interface IHandlerMethodAdapter {


    /**
     *  判断是否支持适配
     *
     * @param handlerMethod the method  to check
     * @param mediaType  request  mediaType
     * @return {@code true} if this resolver supports the supplied method;
     * {@code false} otherwise
     */
    boolean supportsMethod(HandlerMethod handlerMethod, MediaType mediaType);




    /**
     *  在参数绑定之前处理
     * @param request 原始请求
     * @param handlerMethod 适配方法
     * @return
     */
    HttpServletRequest beforeResolver(HttpServletRequest request, HandlerMethod handlerMethod);
}
