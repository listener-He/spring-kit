package org.hehh.cloud.auth.token.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.token.TokenManager;
import org.hehh.cloud.auth.token.impl.jwt.JwtTokenManager;
import org.hehh.cloud.auth.token.impl.redis.RedisJwtTokenManager;
import org.hehh.cloud.auth.token.impl.redis.RedisTokenBeanFactory;
import org.hehh.cloud.auth.token.impl.redis.RedisTokenManager;
import org.hehh.cloud.auth.token.impl.redis.SimpRedisTokenBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author: HeHui
 * @create: 2020-03-15 23:56
 * @description: token 配置
 **/
@Configuration
@EnableConfigurationProperties(TokenParameter.class)
public class TokenConfiguration {




    /**
     *  jwt方式管理token 默认使用此种方式
     *      token.manager = jwt || token.manager = null 时开启
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(TokenManager.class)
    @ConditionalOnProperty(name = "token.manager",havingValue = "jwt",matchIfMissing=true)
    public TokenManager<LoginUser> jwtTokenManager(TokenParameter tokenParameter){
        return new JwtTokenManager(tokenParameter.getSecret(),tokenParameter.getIssuer(),LoginUser.class);
    }




    /**
     *  redis token配置
     */
    @Configuration
    @ConditionalOnClass({RedisTemplate.class})
    @EnableConfigurationProperties(TokenParameter.class)
    static class RedisTokenConfiguration{

        private TokenParameter tokenParameter;

        public RedisTokenConfiguration(TokenParameter tokenParameter){
            this.tokenParameter = tokenParameter;
        }


        /**
         * redis,令牌bean工厂
         *
         * @return {@link RedisTokenBeanFactory}
         */
        @Bean
        @ConditionalOnMissingBean(RedisTokenBeanFactory.class)
        public RedisTokenBeanFactory redisTokenBeanFactory(){
            return new SimpRedisTokenBeanFactory(tokenParameter);
        }



        /**
         *  redis方式管理token
         *      token.manager = redis  时开启
         * @return
         */
        @Bean
        @ConditionalOnMissingBean(TokenManager.class)
        @ConditionalOnProperty(name = "token.manager",havingValue = "redis")
        public TokenManager<LoginUser> redisTokenManager(RedisTokenBeanFactory redisTokenBeanFactory){

            return redisTokenBeanFactory.build(LoginUser.class);
        }





        /**
         *  redis + jwt 方式管理token
         *      token.manager = redis-jwt  时开启
         * @return
         */
        @Bean
        @ConditionalOnMissingBean(TokenManager.class)
        @ConditionalOnProperty(name = "token.manager",havingValue = "redis-jwt")
        public TokenManager redisJwtTokenManager(RedisTokenBeanFactory redisTokenBeanFactory){
           return redisTokenBeanFactory.build(LoginUser.class,tokenParameter.getSecret());
        }
    }







}
