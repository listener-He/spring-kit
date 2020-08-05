package org.hehh.cloud.cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hehh.cloud.cache.CacheConfigurationParameter;
import org.hehh.cloud.cache.CacheParameter;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-07-31 10:24
 * @description: redis-cahce 管理器构建器
 */
public class RedisCacheManagerBuilders {



    /**
     * 构建器
     *
     * @param connectionFactory 连接工厂
     * @param caches         参数
     * @return {@link RedisCacheManager}
     */
    public static RedisCacheManager builder(RedisConnectionFactory connectionFactory, List<CacheParameter> caches){
       return RedisCacheManager.builder(connectionFactory).withInitialCacheConfigurations(getRedisCacheConfigurationMap(caches))
            .cacheWriter(new DefaultRedisCacheWriter(connectionFactory))
            .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
            .build();
    }






    /**
     *  多缓存配置方法
     * @param caches
     * @return
     */
    private static Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap(List<CacheParameter> caches) {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(caches.size());

        if(null != caches){
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            /**
             * 忽略空Bean转json的错误
             */
            om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
            /**
             * 忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
             */
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

            jackson2JsonRedisSerializer.setObjectMapper(om);

            caches.forEach(v -> {
                redisCacheConfigurationMap.put(v.getName(), getRedisCacheConfigurationWithTtl(v.getTtl(),jackson2JsonRedisSerializer));
            });
        }

        return redisCacheConfigurationMap;
    }

    /**
     *  redis缓存配置类
     * @param seconds
     * @return
     */
    private static RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Long seconds,Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
            RedisSerializationContext
                .SerializationPair
                .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }
}
