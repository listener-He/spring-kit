package org.hehh.cloud.cache.ehcache3;

import org.ehcache.Status;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/**
 * @author: HeHui
 * @date: 2020-07-31 01:53
 * @description: ehcache3.0 spring-cache
 */
public class EhCache3Cache<K,V> implements Cache {

    private final org.ehcache.Cache<K,V> cache;

    private final String name;

    /**
     *  构建缓存
     * @param cache
     * @param name
     * @param status
     */
    public EhCache3Cache(org.ehcache.Cache<K, V> cache, String name, Status status) {
        Assert.notNull(cache, "Ehcache must not be null");
        if (!Status.AVAILABLE.equals(status)) {
            throw new IllegalArgumentException("An 'alive' Ehcache is required - current cache is " + status.toString());
        } else {
            this.cache = cache;
        }

        this.name = name;

    }


    /**
     *  获取名称
     * @return
     */
    public final String getName() {
        return this.name;
    }


    /**
     * 获取缓存
     * @return
     */
    public final org.ehcache.Cache<K,V> getNativeCache() {
        return this.cache;
    }


    /**
     *  获取value读取器
     * @param key
     * @return
     */
    @Nullable
    public ValueWrapper get(Object key) {
        Object value = this.getNativeCache().get((K)key);
        return null == value?null:new SimpleValueWrapper(value);
    }


    /**
     *  获取value值 如果没有就将valueLoader放入
     * @param key
     * @param valueLoader
     * @param <T>
     * @return
     */
    @Nullable
    public <T> T get(Object key, Callable<T> valueLoader) {
        try {
            ValueWrapper wrapper = this.get(key);
            if(null != valueLoader){
                return (T)wrapper.get();
            }
        } catch (Exception e) {
        }
        return loadValue((K)key,valueLoader);
    }


    /**
     *  重新load值
     * @param key
     * @param valueLoader
     * @param <T>
     * @return
     */
    private <T> T loadValue(K key, Callable<T> valueLoader) {
        Object value;
        try {
            value = valueLoader.call();
        } catch (Throwable var5) {
            throw new ValueRetrievalException(key, valueLoader, var5);
        }
        this.put(key, value);
        return (T) value;
    }


    /**
     *  获取值
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    @Nullable
    public <T> T get(Object key, @Nullable Class<T> type) {
        Object value = this.getNativeCache().get((K)key);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        } else {
            return (T) value;
        }
    }


    /**
     *  放入值
     * @param key
     * @param value
     */
    public void put(Object key, @Nullable Object value) {
        /**
         *  TODO value值如果为null会报错,待处理
         */
        if(null != value){
            this.cache.put((K)key,(V)value);
        }
    }


    /**
     *  添加
     * @param key
     * @param value
     * @return
     */
    @Nullable
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        if(null != value){
            V v = this.cache.putIfAbsent((K) key, (V) value);
            return new SimpleValueWrapper(v);
        }
        return null;

    }


    /**
     *  删除
     * @param key
     */
    public void evict(Object key) {
        this.cache.remove((K)key);
    }


    /**
     *  清除
     */
    public void clear() {
        this.cache.clear();
    }
}
