package org.hehh.cloud.cache.redis;

import org.hehh.cloud.cache.CacheConfigurationParameter;
import org.hehh.cloud.cache.CacheParameter;
import org.hehh.cloud.cache.ehcache2.EhCache2Builders;
import org.hehh.cloud.cache.ehcache2.EhCache2ConfigurationParameter;
import org.hehh.cloud.cache.ehcache3.EhCache3CacheManager;
import org.hehh.cloud.cache.ehcache3.EhCache3ConfigurationParameter;
import org.hehh.cloud.cache.ehcache3.EhCache3Parameter;
import org.hehh.cloud.cache.ehcache3.MoreCacheManager;
import org.hehh.cloud.cache.topic.CacheNotice;
import org.hehh.cloud.cache.topic.CacheTopicOperations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-07-31 15:53
 * @description: redis 二级缓存管理
 */
public class RedisTwoCacheManager extends AbstractTransactionSupportingCacheManager implements MoreCacheManager {


    private final RedisCacheManager redisCacheManager;

    private final CacheManager cacheManager;

    private final CacheTopicOperations<CacheNotice> topicOperations;

    private final String topicName;


    /**
     * redis - ehcache3.0,两个缓存管理器
     *
     * @param connectionFactory 连接工厂
     * @param parameter         参数
     * @param topicOperations   主题操作
     */
    public RedisTwoCacheManager(RedisConnectionFactory connectionFactory, EhCache3ConfigurationParameter parameter,
                                CacheTopicOperations<CacheNotice> topicOperations){

        this(connectionFactory,(List)parameter.getCaches(),new EhCache3CacheManager(parameter) ,topicOperations,parameter.getTopicName());
    }




    /**
     * redis - ehcache2.0,两个缓存管理器
     *
     * @param connectionFactory 连接工厂
     * @param parameter         参数
     * @param topicOperations   主题操作
     */
    public RedisTwoCacheManager(RedisConnectionFactory connectionFactory, EhCache2ConfigurationParameter parameter,
                                CacheTopicOperations<CacheNotice> topicOperations){
        this(connectionFactory,(List)parameter.getCaches(),new EhCacheCacheManager(EhCache2Builders.builder(parameter)) ,topicOperations,parameter.getTopicName());
    }





    /**
     * 复述,两个缓存管理器
     *  @param connectionFactory 连接工厂
     * @param caches         参数
     * @param oneCacheManager   一个缓存管理器
     * @param topicOperations   主题操作
     * @param topicName
     */
    private RedisTwoCacheManager(RedisConnectionFactory connectionFactory, List<CacheParameter> caches,
                                 AbstractCacheManager oneCacheManager, CacheTopicOperations<CacheNotice> topicOperations, String topicName){
        assert oneCacheManager != null : "一级缓存不能为空";
        oneCacheManager.afterPropertiesSet();

        this.redisCacheManager = RedisCacheManagerBuilders.builder(connectionFactory,caches);
        this.redisCacheManager.afterPropertiesSet();

        this.cacheManager = oneCacheManager;
        this.topicOperations = topicOperations;
        this.topicName = topicName;
    }






    /**
     * Load the initial caches for this cache manager.
     * <p>Called by {@link #afterPropertiesSet()} on startup.
     * The returned collection may be empty but must not be {@code null}.
     */
    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<String> cacheNames = redisCacheManager.getCacheNames();
        if(!CollectionUtils.isEmpty(cacheNames)){
            List<RedisTwoCache> caches = new LinkedList<>();
            for (String name : cacheNames) {
                caches.add(new RedisTwoCache((RedisCache)redisCacheManager.getCache(name),cacheManager.getCache(name),topicName,topicOperations));
            }
            return caches;
        }
        return null;
    }


    /**
     * 主题名称
     *
     * @return {@link String}
     */
    @Override
    public String topicName() {
        return topicName;
    }




    /**
     * 获取缓存管理器
     *
     * @param level 层级
     * @return {@link Optional <CacheManager>}
     */
    @Override
    public Optional<CacheManager> getCacheManager(int level) {
        if(level < 1 || level > 2){
            return Optional.empty();
        }

        return Optional.ofNullable(level == 1 ? cacheManager : redisCacheManager);
    }
}
