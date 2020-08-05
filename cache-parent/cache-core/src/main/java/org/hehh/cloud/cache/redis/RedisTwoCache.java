package org.hehh.cloud.cache.redis;

import org.hehh.cloud.cache.TwoCache;
import org.hehh.cloud.cache.topic.CacheNotice;
import org.hehh.cloud.cache.topic.CacheTopicOperations;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;

/**
 * @author: HeHui
 * @date: 2020-07-31 11:06
 * @description: redis 二级缓存
 */
public class RedisTwoCache extends TwoCache {

    /**
     * Create an {@code AbstractValueAdaptingCache} with the given setting.
     *
     * @param twoCache
     * @param oneCache
     * @param topicName
     * @param topicOperations
     */
    public RedisTwoCache(RedisCache twoCache, Cache oneCache, String topicName, CacheTopicOperations<CacheNotice> topicOperations) {
        super(twoCache, oneCache, topicName, topicOperations);
    }
}
