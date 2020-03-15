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
import org.hehh.cloud.auth.token.impl.redis.RedisTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TokenConditional {


    @Autowired
    private TokenParameter tokenParameter;


    /**
     *  jwt方式管理token 默认使用此种方式
     *      token.manager = jwt || token.manager = null 时开启
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(TokenManager.class)
    @ConditionalOnProperty(name = "token.manager",havingValue = "jwt",matchIfMissing=true)
    public TokenManager jwtTokenManager(){
        return new JwtTokenManager(tokenParameter.getSecret(),tokenParameter.getIssuer());
    }


    /**
     *  获取redis序列化
     * @return
     */
    private GenericJackson2JsonRedisSerializer getRedisSerializer(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }


    /**
     *  redis数据源配置
     * @return
     */
    private RedisConnectionFactory redisConnectionFactory(){
        RedisProperties redisProperties = tokenParameter.getRedis();

        Assert.notNull(redisProperties,"token redisManager的配置不能为空");



        /**
         *  redis配置
         */
        RedisStandaloneConfiguration  redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());


        /**
         *  连接池配置
         */
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        genericObjectPoolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());


        /**
         * redis客户端配置
         */
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder =  LettucePoolingClientConfiguration.builder().
                commandTimeout(redisProperties.getTimeout());

        builder.shutdownTimeout(redisProperties.getLettuce().getShutdownTimeout());
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();


        /**
         *  创建源
         */
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();


        return lettuceConnectionFactory;

    }




    /**
     *  redis方式管理token
     *      token.manager = redis  时开启
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    @ConditionalOnProperty(name = "token.manager",havingValue = "redis")
    public TokenManager redisTokenManager(){
        RedisTemplate<String, LoginUser> template = new RedisTemplate<>();

        GenericJackson2JsonRedisSerializer redisSerializer = getRedisSerializer();
        /**
         * value值的序列化采用 obj
         */
        template.setValueSerializer(redisSerializer);

        /**
         * key的序列化采用StringRedisSerializer
         */
        template.setKeySerializer(new StringRedisSerializer());

        /**
         *  连接池
         */
        template.setConnectionFactory(redisConnectionFactory());



        return new RedisTokenManager(template);
    }





    /**
     *  redis + jwt 方式管理token
     *      token.manager = redis-jwt  时开启
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    @ConditionalOnProperty(name = "token.manager",havingValue = "redis-jwt")
    public TokenManager redisJwtTokenManager(){
        RedisTemplate<String, Long> template = new RedisTemplate<>();

        /**
         * value值的序列化采用 jdk
         */
        template.setValueSerializer(new JdkSerializationRedisSerializer());

        /**
         * key的序列化采用StringRedisSerializer
         */
        template.setKeySerializer(new StringRedisSerializer());

        /**
         *  连接池
         */
        template.setConnectionFactory(redisConnectionFactory());



        return new RedisJwtTokenManager(tokenParameter.getSecret(),tokenParameter.getIssuer(),template);
    }



}
