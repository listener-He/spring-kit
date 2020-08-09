package org.hehh.cloud.spring.mvc.resolver;

import org.hehh.utils.http.userAgent.UserAgent;
import org.hehh.utils.http.userAgent.UserAgentProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * @author: HeHui
 * @date: 2020-08-06 02:01
 * @description: 客户端解析
 */
public class UserAgentArgumentResolver implements HandlerMethodArgumentResolver {


    private final UserAgentProcessor userAgentProcessor;

    public UserAgentArgumentResolver(UserAgentProcessor userAgentProcessor){
        this.userAgentProcessor = userAgentProcessor;
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
        return UserAgent.class.equals(parameter.getParameterType());
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
        if(userAgentProcessor == null){
            return null;
        }
        String[] values = webRequest.getHeaderValues(HttpHeaders.USER_AGENT);
        if(values != null && values.length > 0){
            String userAgent = values[0];
            return userAgentProcessor.parse(userAgent);
        }

        return null;
    }


}
