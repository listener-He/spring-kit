package org.hehh.wechat.config;

import org.hehh.utils.http.HttpRequestProxy;
import org.hehh.weChat.AuthStorage;
import org.hehh.weChat.RequestAuth;
import org.hehh.weChat.RequestAuthImpl;
import org.hehh.weChat.SimpAuthStorage;
import org.hehh.wechat.RedisAuthStorage;
import org.hehh.wechat.WxConfigurationMoreParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;

/**
 * @author: HeHui
 * @date: 2020-08-09 15:27
 * @description: 微信授权配置
 */
@Configuration
public class WxOauth2Configuration {

    /**
     *  微信配置参数
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.wx")
    @ConditionalOnMissingBean
    public WxConfigurationMoreParameter moreParameter(){
        return new WxConfigurationMoreParameter();
    }


    /**
     * 请求身份验证
     *
     * @param httpProxy    http代理
     * @param tokenStorage 令牌存储
     * @param parameter    参数
     * @return {@link RequestAuth}
     */
    @Bean
    @ConditionalOnMissingBean(RequestAuth.class)
    public RequestAuth requestAuth(@Autowired HttpRequestProxy httpProxy, AuthStorage tokenStorage, WxConfigurationMoreParameter parameter){
        return new RequestAuthImpl(httpProxy,tokenStorage,parameter.getApps());
    }


    /**
     * 简单身份验证存储
     *
     * @return {@link AuthStorage}
     */
    @Bean
    @ConditionalOnMissingBean(AuthStorage.class)
    public AuthStorage simpAuthStorage(){
        return new SimpAuthStorage();
    }



    /**
     * 微信redis 授权配置
     */
    @Configuration
    @ConditionalOnClass(RedisOperations.class)
    static class RedisWxOauth2Configuration{


        /**
         *  微信授权存储
         * @param redisOperations
         * @return
         */
        @Bean
        @ConditionalOnProperty(prefix = "spring.wx",name = "storage",havingValue = "redis")
        @ConditionalOnMissingBean(AuthStorage.class)
        public AuthStorage redisAuthStorage(RedisOperations<String,String> redisOperations){
            return new RedisAuthStorage(redisOperations);
        }
    }
}
