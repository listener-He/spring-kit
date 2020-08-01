package org.hehh.cloud.cache.config;

import net.sf.ehcache.Cache;
import org.hehh.cloud.cache.CacheConfigurationParameter;
import org.hehh.cloud.cache.MoreCacheManager;
import org.hehh.cloud.cache.ehcache2.EhCache2Builders;
import org.hehh.cloud.cache.ehcache2.EhCache2ConfigurationParameter;
import org.hehh.cloud.cache.ehcache2.EhCache2Parameter;
import org.hehh.cloud.cache.ehcache3.EhCache3CacheManager;
import org.hehh.cloud.cache.ehcache3.EhCache3ConfigurationParameter;
import org.hehh.cloud.cache.ehcache3.EhCache3Parameter;
import org.hehh.cloud.cache.redis.RedisTwoCacheManager;
import org.hehh.cloud.cache.topic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
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
@EnableCaching
public class CacheConfiguration {


    /**
     * ehcache2配置参数
     *
     * @return {@link EhCache2ConfigurationParameter}
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.cache.ehcache2")
    @ConditionalOnClass({ Cache.class})
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
    @ConditionalOnClass(org.ehcache.CacheManager.class)
    @ConditionalOnMissingBean(EhCache3ConfigurationParameter.class)
    public EhCache3ConfigurationParameter ehCache3ConfigurationParameter(){
        return new EhCache3ConfigurationParameter();
    }


    /**
     * ehcache2缓存管理器
     *
     * @param parameter 参数
     * @return {@link CacheManager}
     */
    @Bean
    @ConditionalOnMissingBean({MoreCacheManager.class,CacheManager.class})
    @ConditionalOnProperty(prefix = "spring.cache",name = "enable",havingValue = "ehcache2")
    public CacheManager ehCache2CacheManager(EhCache2ConfigurationParameter parameter){
        return new EhCacheCacheManager(EhCache2Builders.builder(parameter));
    }


    /**
     * ehcache2缓存管理器
     *
     * @param parameter 参数
     * @return {@link CacheManager}
     */
    @Bean
    @ConditionalOnMissingBean({MoreCacheManager.class,CacheManager.class})
    @ConditionalOnProperty(prefix = "spring.cache",name = "enable",havingValue = "ehcache3")
    public CacheManager ehCache3CacheManager(EhCache3ConfigurationParameter parameter){
        return new EhCache3CacheManager(parameter);
    }



    /**
     * redis,缓存配置
     *
     * @author hehui
     * @date 2020/08/01
     */
    @Configuration
    @ConditionalOnClass(RedisConnectionFactory.class)
    @AutoConfigureAfter(CacheConfiguration.class)
    @ConditionalOnProperty(prefix = "spring.cache",name = "two",havingValue = "redis")
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
        @ConditionalOnProperty(prefix = "spring.cache",name = "one",havingValue = "ehcache3")
        @ConditionalOnMissingBean(MoreCacheManager.class)
        public CacheManager redisTowEhcache3CacheManager(RedisConnectionFactory connectionFactory,EhCache3ConfigurationParameter parameter, @Autowired(required = false) CacheTopicOperations<CacheNotice> topicOperations){
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
        @ConditionalOnProperty(prefix = "spring.cache",name = "one",havingValue = "ehcache2")
        @ConditionalOnMissingBean(MoreCacheManager.class)
        public CacheManager redisTowEhcache2CacheManager(RedisConnectionFactory connectionFactory,EhCache2ConfigurationParameter parameter, @Autowired(required = false) CacheTopicOperations<CacheNotice> topicOperations){
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
        @ConditionalOnProperty(prefix = "spring.cache",name = "topic",havingValue = "redis")
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
        @ConditionalOnProperty(prefix = "spring.cache",name = "topic",havingValue = "redis")
        @ConditionalOnMissingBean(CacheTopicAdapter.class)
        public CacheTopicAdapter cacheTopicAdapter(RedisConnectionFactory connectionFactory,RedisOperations<String, String> redisOperations, MoreCacheManager cacheManager){
            return new RedisCacheTopicAdapter(redisOperations.getValueSerializer(),connectionFactory,cacheManager,cacheManager.topicName());
        }

    }


}
