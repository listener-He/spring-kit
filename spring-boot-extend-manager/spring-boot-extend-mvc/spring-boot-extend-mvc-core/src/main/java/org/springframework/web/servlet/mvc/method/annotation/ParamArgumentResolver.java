package org.springframework.web.servlet.mvc.method.annotation;

import org.hehh.cloud.spring.mvc.annotation.Param;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-04-04 15:20
 * @description: 参数解析器
 */
public class ParamArgumentResolver extends RequestResponseBodyMethodProcessor {


    /**
     * Basic constructor with converters only. Suitable for resolving
     * {@code @RequestBody}. For handling {@code @ResponseBody} consider also
     * providing a {@code ContentNegotiationManager}.
     *
     * @param converters
     */
    public ParamArgumentResolver(List<HttpMessageConverter<?>> converters) {
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
    public ParamArgumentResolver(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager) {
        super(converters, manager);
    }

    /**
     * Complete constructor for resolving {@code @RequestBody} method arguments.
     * For handling {@code @ResponseBody} consider also providing a
     * {@code ContentNegotiationManager}.
     *
     * @param converters
     * @param requestResponseBodyAdvice
     * @since 4.2
     */
    public ParamArgumentResolver(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice) {
        super(converters, requestResponseBodyAdvice);
    }


    /**
     * Complete constructor for resolving {@code @RequestBody} and handling
     * {@code @ResponseBody}.
     *
     * @param converters
     * @param manager
     * @param requestResponseBodyAdvice
     */
    public ParamArgumentResolver(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, manager, requestResponseBodyAdvice);
    }



    /**
     * Whether the given {@linkplain MethodParameter method parameter} is
     * supported by this resolver.
     *
     * @param parameter the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Param.class);
    }

    /**
     * Resolves a method parameter into an argument value from a given request.
     * A {@link ModelAndViewContainer} provides access to the model for the
     * request. A {@link WebDataBinderFactory} provides a way to create
     * a {@link WebDataBinder} instance when needed for data binding and
     * type conversion purposes.
     *
     * @param parameter     the method parameter to resolve. This parameter must
     *                      have previously been passed to {@link #supportsParameter} which must
     *                      have returned {@code true}.
     * @param mavContainer  the ModelAndViewContainer for the current request
     * @param webRequest    the current request
     * @param binderFactory a factory for creating {@link WebDataBinder} instances
     * @return the resolved argument value, or {@code null} if not resolvable
     * @throws Exception in case of errors with the preparation of argument values
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        return super.resolveArgument(parameter,mavContainer,webRequest,binderFactory);
    }




    /**
     * Handle the given return value by adding attributes to the model and
     * setting a view or setting the
     * {@link ModelAndViewContainer#setRequestHandled} flag to {@code true}
     * to indicate the response has been handled directly.
     *
     * @param returnValue  the value returned from the handler method
     * @param returnType   the type of the return value. This type must have
     *                     previously been passed to {@link #supportsReturnType} which must
     *                     have returned {@code true}.
     * @param mavContainer the ModelAndViewContainer for the current request
     * @param webRequest   the current request
     * @throws Exception if the return value handling results in an error
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {

    }
}
