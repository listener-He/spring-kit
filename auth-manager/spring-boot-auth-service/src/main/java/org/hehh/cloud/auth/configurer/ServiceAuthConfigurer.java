package org.hehh.cloud.auth.configurer;

import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.holder.TTLUserThreadLocalHolder;
import org.hehh.cloud.auth.holder.UserHolder;
import org.hehh.cloud.auth.interceptor.LoginInterceptor;
import org.hehh.cloud.auth.interceptor.ServiceUserParsingInterceptor;
import org.hehh.cloud.auth.parameter.LoginUserParsingParameter;
import org.hehh.cloud.auth.resolver.UserResolver;
import org.hehh.cloud.auth.token.TokenManager;
import org.hehh.cloud.auth.token.core.TokenConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-09-16 01:43
 * @description: 服务端auth配置
 */
@AutoConfigureAfter(TokenConfiguration.class)
public class ServiceAuthConfigurer  {




    /**
     * 用户持有人
     *
     * @return {@link UserHolder<LoginUser>}
     */
    @Bean
    @ConditionalOnMissingBean(UserHolder.class)
    public UserHolder<LoginUser> userHolder(){
        return new TTLUserThreadLocalHolder<>();
    }


    /**
     * 服务身份验证web配置
     *
     * @author hehui
     * @date 2020/09/16
     */
    @Configuration
    @ConditionalOnWebApplication
    @AutoConfigureAfter(ServiceAuthConfigurer.class)
    @EnableConfigurationProperties(LoginUserParsingParameter.class)
    @ConditionalOnBean(TokenManager.class)
    static class ServiceAuthWebConfigurer implements WebMvcConfigurer{

        /**
         * 解析参数
         */
        private final LoginUserParsingParameter parsingParameter;

        /**
         * 令牌管理器
         */
        private final TokenManager<LoginUser> tokenManager;


        /**
         * 用户持有人
         */
        private final UserHolder<LoginUser> userHolder;


        /**
         * 服务身份验证web配置
         *  @param parsingParameter 解析参数
         * @param tokenManager     令牌管理器
         * @param userHolder
         */
        public ServiceAuthWebConfigurer(LoginUserParsingParameter parsingParameter, TokenManager<LoginUser> tokenManager, UserHolder<LoginUser> userHolder) {
            this.parsingParameter = parsingParameter;
            this.tokenManager = tokenManager;
            this.userHolder = userHolder;
        }

        /**
         * 添加拦截器
         *
         * @param registry 注册表
         */
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new ServiceUserParsingInterceptor<>(parsingParameter.getTokenName(),userHolder,tokenManager));
            registry.addInterceptor(new LoginInterceptor(userHolder));
        }


        /**
         * 参数解析器
         *
         * @param argumentResolvers
         */
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(0,new UserResolver<LoginUser>(userHolder,LoginUser.class));
        }
    }







}
