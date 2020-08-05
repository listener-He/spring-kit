package org.hehh.cloud.spring.mvc.resolver;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentParser;
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
        return org.hehh.cloud.spring.mvc.UserAgent.class.equals(parameter.getParameterType());
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
        String[] values = webRequest.getHeaderValues(HttpHeaders.USER_AGENT);
        if(values != null && values.length > 0){
            String userAgent = values[0];
            UserAgent parse = UserAgentParser.parse(userAgent);

            org.hehh.cloud.spring.mvc.UserAgent  agent = new org.hehh.cloud.spring.mvc.UserAgent();

            agent.setMobile(parse.isMobile());

            if(parse.isMobile()){
                agent.setMobileSystem(parse.getPlatform().getName());
                agent.setName(parse.getPlatform().getName());
            }else{
                agent.setName(parse.getBrowser().getName());
            }

            agent.setVersion(parse.getVersion());

            return agent;
        }

        return null;
    }


}
