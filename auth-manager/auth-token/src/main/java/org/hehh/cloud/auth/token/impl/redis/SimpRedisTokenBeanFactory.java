package org.hehh.cloud.auth.token.impl.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.token.TokenManager;
import org.hehh.cloud.auth.token.core.TokenParameter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author: HeHui
 * @date: 2020-08-06 00:59
 * @description: 简单的模式
 */
public class SimpRedisTokenBeanFactory  implements RedisTokenBeanFactory , InitializingBean {

    private final TokenParameter tokenParameter;

    private RedisConnectionFactory redisConnectionFactory;

    private final RedisSerializer valueRedisSerializer;

    private final RedisSerializer keyRedisSerializer;



    /**
     * 简单redis,令牌bean工厂
     *
     * @param tokenParameter 令牌参数
     */
    public SimpRedisTokenBeanFactory(TokenParameter tokenParameter){
        this.tokenParameter = tokenParameter;
        this.valueRedisSerializer = getRedisSerializer();
        this.keyRedisSerializer = new StringRedisSerializer();
    }




    /**
     * 得到redis,连接工厂
     *
     * @return {@link RedisConnectionFactory}
     */
    @Override
    public RedisConnectionFactory getRedisConnectionFactory() {
        return redisConnectionFactory;
    }

    /**
     * 得到redis,value序列化器
     *
     * @return {@link RedisSerializer <?>}
     */
    @Override
    public RedisSerializer<?> getValueRedisSerializer() {
        return valueRedisSerializer;
    }

    /**
     * 获得关键的序列化器
     *
     * @return {@link RedisSerializer<?>}
     */
    @Override
    public RedisSerializer<?> getKeySerializer() {
        return keyRedisSerializer;
    }


    /**
     * 构建
     *
     * @param userClass 用户类
     * @param secret    秘密
     * @return {@link TokenManager<T>}
     * @see RedisJwtTokenManager
     */
    @Override
    public <T extends LoginUser> TokenManager<T> build(Class<T> userClass, String secret) {

        if(StringUtils.isEmpty(secret)){
            secret = tokenParameter.getSecret();
        }
        RedisTemplate<String, Long> template = new RedisTemplate<>();

        /**
         * value值的序列化采用 obj
         */
        template.setValueSerializer(valueRedisSerializer);

        /**
         * key的序列化采用StringRedisSerializer
         */
        template.setKeySerializer(keyRedisSerializer);

        /**
         *  连接池
         */
        template.setConnectionFactory(redisConnectionFactory);

        return new RedisJwtTokenManager<T>(secret,tokenParameter.getIssuer(),template,userClass);
    }





    /**
     * 构建
     *
     * @param userClass
     * @return {@link TokenManager<T>}
     * @see RedisTokenManager
     */
    @Override
    public <T extends LoginUser> TokenManager<T> build(Class<T> userClass) {
        RedisTemplate<String, T> template = new RedisTemplate<>();

        /**
         * value值的序列化采用 obj
         */
        template.setValueSerializer(valueRedisSerializer);

        /**
         * key的序列化采用StringRedisSerializer
         */
        template.setKeySerializer(keyRedisSerializer);

        /**
         *  连接池
         */
        template.setConnectionFactory(redisConnectionFactory);

        return new RedisTokenManager<T>(template);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.redisConnectionFactory = getRedisConnectionFactory();
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
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
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
}
