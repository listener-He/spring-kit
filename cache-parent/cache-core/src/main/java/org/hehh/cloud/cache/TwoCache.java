package org.hehh.cloud.cache;

import org.hehh.cloud.cache.topic.CacheNotice;
import org.hehh.cloud.cache.topic.CacheTopicOperations;
import org.hehh.cloud.cache.topic.NoticeType;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @author: HeHui
 * @date: 2020-07-31 15:23
 * @description: 二级缓存
 */
public class TwoCache extends AbstractValueAdaptingCache implements MoreCache {

    private final Cache twoCache;

    private final Cache oneCache;

    private final String topicName;


    private final CacheTopicOperations<CacheNotice> topicOperations;


    /**
     * Create an {@code AbstractValueAdaptingCache} with the given setting.
     *
     */
    protected TwoCache(Cache twoCache, Cache oneCache, String topicName, CacheTopicOperations<CacheNotice> topicOperations) {
        super(true);
        assert oneCache != null : "Level 1 cache not null";
        assert twoCache != null : "Level 2 cache not null";
        this.twoCache = twoCache;
        this.oneCache = oneCache;
        this.topicName = topicName;
        this.topicOperations = topicOperations;

        oneCache.clear();
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
     * 得到缓存
     *
     * @param level 级别
     * @return {@link Optional < Cache >}
     */
    @Override
    public Optional<Cache> getCache(int level) {
        if(level > 2 || level < 1){
            return Optional.empty();
        }
        return Optional.ofNullable( 1 == level ? oneCache : twoCache);
    }




    /**
     * Perform an actual lookup in the underlying store.
     *
     * @param key the key whose associated value is to be returned
     * @return the raw store value for the key, or {@code null} if none
     */
    @Override
    protected Object lookup(Object key) {
        ValueWrapper valueWrapper = oneCache.get(key);
        if(valueWrapper == null){
            valueWrapper = twoCache.get(key);
        }
        return valueWrapper == null ? null : valueWrapper.get();
    }

    /**
     * Return the cache name.
     */
    @Override
    public String getName() {
        return oneCache.getName();
    }

    /**
     * Return the underlying native cache provider.
     */
    @Override
    public Object getNativeCache() {
        return twoCache.getNativeCache();
    }

    /**
     * Return the value to which this cache maps the specified key, obtaining
     * that value from {@code valueLoader} if necessary. This method provides
     * a simple substitute for the conventional "if cached, return; otherwise
     * create, cache and return" pattern.
     * <p>If possible, implementations should ensure that the loading operation
     * is synchronized so that the specified {@code valueLoader} is only called
     * once in case of concurrent access on the same key.
     * <p>If the {@code valueLoader} throws an exception, it is wrapped in
     * a {@link ValueRetrievalException}
     *
     * @param key         the key whose associated value is to be returned
     * @param valueLoader
     * @return the value to which this cache maps the specified key
     * @throws ValueRetrievalException if the {@code valueLoader} throws an exception
     * @see #get(Object)
     * @since 4.3
     */
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T t = oneCache.get(key, valueLoader);
        if(t == null){
            t = twoCache.get(key,valueLoader);

            /**
             *  if local cache is null and redis cache not null ?
             */
//            if(t != null){
//                oneCache.put(key,t);
//            }
        }
        return t;
    }

    /**
     * Associate the specified value with the specified key in this cache.
     * <p>If the cache previously contained a mapping for this key, the old
     * value is replaced by the specified value.
     * <p>Actual registration may be performed in an asynchronous or deferred
     * fashion, with subsequent lookups possibly not seeing the entry yet.
     * This may for example be the case with transactional cache decorators.
     * Use {@link #putIfAbsent} for guaranteed immediate registration.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @see #putIfAbsent(Object, Object)
     */
    @Override
    public void put(Object key, Object value) {
        twoCache.put(key,value);
        /**
         * other local cache  put?
         */
        sendPut(key,value);
    }

    /**
     * Evict the mapping for this key from this cache if it is present.
     * <p>Actual eviction may be performed in an asynchronous or deferred
     * fashion, with subsequent lookups possibly still seeing the entry.
     * This may for example be the case with transactional cache decorators.
     * Use {@link #evictIfPresent} for guaranteed immediate removal.
     *
     * @param key the key whose mapping is to be removed from the cache
     * @see #evictIfPresent(Object)
     */
    @Override
    public void evict(Object key) {
        twoCache.evict(key);
        /**
         * other local cache  evict?
         */
        sendEvict(key);
    }

    /**
     * Clear the cache through removing all mappings.
     * <p>Actual clearing may be performed in an asynchronous or deferred
     * fashion, with subsequent lookups possibly still seeing the entries.
     * This may for example be the case with transactional cache decorators.
     * Use {@link #invalidate()} for guaranteed immediate removal of entries.
     *
     * @see #invalidate()
     */
    @Override
    public void clear() {
        twoCache.clear();
        /**
         * other local cache  clear?
         */
        sendClear();
    }











    /**
     *  添加
     *
     * @param key 关键
     * @param value
     */
    private void sendPut(Object key, Object value){
        if(!send(NoticeType.PUT,key)){
            oneCache.put(key,value);
        }
    }


    /**
     * 发送清除
     *
     */
    private void sendClear(){
        if(!send(NoticeType.CLEAR,null)){
            oneCache.clear();
        }
    }

    /**
     *  删除
     *
     * @param key 关键
     */
    private void sendEvict(Object key){
        if(!send(NoticeType.EVICT,key)){
            oneCache.evict(key);
        }
    }


    /**
     * 发送
     *
     * @param type 类型
     * @param key  关键
     */
    private boolean send(NoticeType type,Object key){
         if(topicOperations != null){
             topicOperations.send(topicName, CacheNotice.create(type,key,this.getName(),2));
            return true;
         }
         return false;
    }
}
