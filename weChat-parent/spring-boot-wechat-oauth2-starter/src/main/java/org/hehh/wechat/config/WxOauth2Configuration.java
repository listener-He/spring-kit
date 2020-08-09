package org.hehh.wechat.config;

import org.hehh.utils.http.HttpRequest;
import org.hehh.utils.http.HuToolHttpRequest;
import org.hehh.weChat.AuthStorage;
import org.hehh.weChat.RequestAuth;
import org.hehh.weChat.RequestAuthImpl;
import org.hehh.weChat.WxHttpProxy;
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



    @Bean
    @ConditionalOnMissingBean(RequestAuth.class)
    public RequestAuth requestAuth(@Autowired(required = false) HttpRequest httpProxy, AuthStorage tokenStorage, WxConfigurationMoreParameter parameter){
        if(httpProxy == null){
            httpProxy = new HuToolHttpRequest();
        }
        return new RequestAuthImpl(httpProxy,tokenStorage,parameter.getApps());
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
