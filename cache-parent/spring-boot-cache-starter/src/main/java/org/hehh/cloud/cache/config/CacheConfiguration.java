package org.hehh.cloud.cache.config;

import org.hehh.cloud.cache.MoreCacheManager;
import org.hehh.cloud.cache.ehcache2.EhCache2ConfigurationParameter;
import org.hehh.cloud.cache.ehcache3.EhCache3ConfigurationParameter;
import org.hehh.cloud.cache.redis.RedisTwoCacheManager;
import org.hehh.cloud.cache.topic.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.StringUtils;

/**
 * @author: HeHui
 * @date: 2020-08-01 16:26
 * @description: 缓存配置
 */
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
@EnableCaching
public class CacheConfiguration {


    /**
     * ehcache2配置参数
     *
     * @return {@link EhCache2ConfigurationParameter}
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.cache.ehcache2")
    @ConditionalOnProperty(prefix = "spring.cache.one",havingValue = "ehcache2")
    @ConditionalOnMissingBean(EhCache3ConfigurationParameter.class)
    public EhCache2ConfigurationParameter ehCache2ConfigurationParameter(){
        return new EhCache2ConfigurationParameter();
    }


    /**
     * ehcache3配置参数
     *
     * @return {@link EhCache2ConfigurationParameter}
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.cache.ehcache3")
    @ConditionalOnProperty(prefix = "spring.cache.one",havingValue = "ehcache3")
    @ConditionalOnMissingBean(EhCache3ConfigurationParameter.class)
    public EhCache3ConfigurationParameter ehCache3ConfigurationParameter(){
        return new EhCache3ConfigurationParameter();
    }


    /**
     * redis,缓存配置
     *
     * @author hehui
     * @date 2020/08/01
     */
    @Configuration
    @ConditionalOnClass(RedisConnectionFactory.class)
    @ConditionalOnBean(RedisConnectionFactory.class)
    static class RedisCacheConfiguration{


        /**
         *  服务名
         */
        @Value("${spring.application.name}")
        private String defaultTopic;



        /**
         * redis,ehcache3缓存管理器
         *
         * @param connectionFactory 连接工厂
         * @param parameter         参数
         * @param topicOperations   主题操作
         * @return {@link CacheManager}
         */
        @Bean
        @Primary
        @ConditionalOnProperty(prefix = "spring.cache.two",havingValue = "redis")
        @ConditionalOnBean(EhCache3ConfigurationParameter.class)
        @ConditionalOnMissingBean(RedisTwoCacheManager.class)
        public CacheManager redisTowEhcache3CacheManager(RedisConnectionFactory connectionFactory,EhCache3ConfigurationParameter parameter, CacheTopicOperations<CacheNotice> topicOperations){
            if(StringUtils.isEmpty(parameter.getTopicName())){
                parameter.setTopicName(defaultTopic);
            }
            return new RedisTwoCacheManager(connectionFactory,parameter,topicOperations);
        }



        /**
         * redis,ehcache2缓存管理器
         *
         * @param connectionFactory 连接工厂
         * @param parameter         参数
         * @param topicOperations   主题操作
         * @return {@link CacheManager}
         */
        @Bean
        @Primary
        @ConditionalOnProperty(prefix = "spring.cache.two",havingValue = "redis")
        @ConditionalOnBean(EhCache2ConfigurationParameter.class)
        @ConditionalOnMissingBean(RedisTwoCacheManager.class)
        public CacheManager redisTowEhcache2CacheManager(RedisConnectionFactory connectionFactory,EhCache2ConfigurationParameter parameter, CacheTopicOperations<CacheNotice> topicOperations){
            if(StringUtils.isEmpty(parameter.getTopicName())){
                parameter.setTopicName(defaultTopic);
            }
            return new RedisTwoCacheManager(connectionFactory,parameter,topicOperations);
        }


        /**
         * 缓存主题操作
         *
         * @param redisOperations 复述,操作
         * @return {@link CacheTopicOperations<CacheNotice>}
         */
        @Bean
        @ConditionalOnProperty(prefix = "spring.cache.topic",havingValue = "redis")
        @ConditionalOnMissingBean(CacheTopicOperations.class)
        public CacheTopicOperations<CacheNotice> cacheTopicOperations(RedisOperations<String, String> redisOperations){
            return new RedisCacheTopicOperations(redisOperations);
        }


        /**
         * 缓存主题适配器
         *
         * @param connectionFactory 连接工厂
         * @param redisOperations   复述,操作
         * @param cacheManager      缓存管理器
         * @return {@link CacheTopicAdapter}
         */
        @Bean
        @ConditionalOnProperty(prefix = "spring.cache.topic",havingValue = "redis")
        @ConditionalOnMissingBean(CacheTopicAdapter.class)
        public CacheTopicAdapter cacheTopicAdapter(RedisConnectionFactory connectionFactory,RedisOperations<String, String> redisOperations, MoreCacheManager cacheManager){
            return new RedisCacheTopicAdapter(redisOperations.getValueSerializer(),connectionFactory,cacheManager,cacheManager.topicName());
        }

    }


}
