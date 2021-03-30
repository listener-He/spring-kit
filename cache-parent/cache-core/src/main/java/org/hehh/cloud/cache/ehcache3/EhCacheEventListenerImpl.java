package org.hehh.cloud.cache.ehcache3;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.EventType;
import org.ehcache.impl.events.CacheEventAdapter;

/**
 * @author  HeHui
 * @create: 2019-09-29 15:10
 * @description ehcache 3.0及以上 缓存监听器
 **/
public class EhCacheEventListenerImpl<K,V> extends CacheEventAdapter<K,V> {


    /**
     * Invoked when a {@link CacheEvent} for an {@link EventType#EVICTED eviction} is received.
     *     驱逐
     * @param key          the evicted key
     * @param evictedValue the evicted value
     */
    @Override
    protected void onEviction(K key, V evictedValue) {
        super.onEviction(key, evictedValue);
    }

    /**
     * Invoked when a {@link CacheEvent} for an {@link EventType#EXPIRED expiration} is received.
     *   到期
     * @param key          the expired key
     * @param expiredValue the expired value
     */
    @Override
    protected void onExpiry(K key, V expiredValue) {
        super.onExpiry(key, expiredValue);
        System.out.println(key+" 过期了");
    }

    /**
     * Invoked when a {@link CacheEvent} for a {@link EventType#REMOVED removal} is received.
     *  删除
     * @param key          the removed key
     * @param removedValue the removed value
     */
    @Override
    protected void onRemoval(K key, V removedValue) {
        super.onRemoval(key, removedValue);
    }

    /**
     * Invoked when a {@link CacheEvent} for an {@link EventType#UPDATED update} is received.
     *  更改
     * @param key      the updated key
     * @param oldValue the previous value
     * @param newValue the updated value
     */
    @Override
    protected void onUpdate(K key, V oldValue, V newValue) {
        super.onUpdate(key, oldValue, newValue);
    }

    /**
     * Invoked when a {@link CacheEvent} for a {@link EventType#CREATED creation} is received.
     * 创建
     * @param key      the created key
     * @param newValue the created value
     */
    @Override
    protected void onCreation(K key, V newValue) {
        super.onCreation(key, newValue);
    }
}
