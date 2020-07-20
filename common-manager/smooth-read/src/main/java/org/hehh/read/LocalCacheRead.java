package org.hehh.read;

import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author: HeHui
 * @date: 2020-07-20 17:11
 * @description: 本地缓存阅读数
 */
public class LocalCacheRead<ID> extends ReadAbstract<Double,ID>{


    private Cache cache;

    /**
     *  缓存前缀
     */
    private final String cacheKey;

    /**
     * 阅读文摘
     *
     * @param maxRead     最大阅读数
     * @param jobSeconds  定时秒
     * @param readStorage 读取存储
     */
    protected LocalCacheRead(Double maxRead, int jobSeconds, ReadStorage<Double, ID> readStorage,String cacheKey) {
        super(maxRead, jobSeconds, readStorage);
        this.cacheKey = cacheKey;
        cache = new ConcurrentMapCache(cacheKey,false);
    }


    /**
     * 增加
     *
     * @param key 关键
     * @param n   阅读数
     * @return {@link Double} 返回阅读数
     */
    @Override
    Optional<Double> increase(ID key, Double n) {
        Double aDouble = cache.get(key, Double.class);
        if(aDouble == null){
            aDouble = 0D;
        }

        cache.put(key,aDouble + n);

        return Optional.ofNullable(aDouble + n);
    }



    /**
     * 减少
     *
     * @param key 关键
     * @param n   阅读数
     */
    @Override
    void reduce(ID key, Double n) {
        Double aDouble = cache.get(key, Double.class);
        if(aDouble != null){
            if(aDouble <= n){
                cache.evict(key);
                return;
            }
            cache.put(key,aDouble - n);
        }
    }


    /**
     * 得到所有
     *
     * @return {@link Map <ID, T>}
     */
    @Override
    Optional<Map<ID, Double>> getAll() {

        return Optional.empty();
    }

    /**
     * 清除
     */
    @Override
    void clear() {

    }

    /**
     * 查询指定key的阅读数
     *
     * @param key 阅读key
     * @return {@link Optional<Double>}
     */
    @Override
    public Optional<Double> getRead(ID key) {
        return Optional.empty();
    }

    /**
     * 查询多个key的阅读数
     *
     * @param keys 阅读keys
     * @return {@link Optional<Map<ID,Double>>}
     */
    @Override
    public Optional<Map<ID, Double>> getReads(List<ID> keys) {
        return Optional.empty();
    }
}
