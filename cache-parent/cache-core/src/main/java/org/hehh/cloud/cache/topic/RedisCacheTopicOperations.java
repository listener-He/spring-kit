package org.hehh.cloud.cache.topic;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.StringUtils;

/**
 * @author  HeHui
 * @date  2020-08-01 14:55
 * @description redis 实现 缓存 topic 操作
 */
public class RedisCacheTopicOperations implements CacheTopicOperations<CacheNotice> {


    private final RedisOperations<String,String> redisOperations;

    /**
     * 复述,缓存主题操作
     *
     * @param redisOperations 复述,操作
     */
    public RedisCacheTopicOperations(RedisOperations<String, String> redisOperations) {
        this.redisOperations = redisOperations;
    }



    /**
     * 发送
     *
     * @param topic  主题
     * @param notice 数据
     */
    @Override
    public void send(String topic, CacheNotice notice) {
        if(StringUtils.hasText(topic)){
            redisOperations.convertAndSend(topic,notice);
        }
    }
}
