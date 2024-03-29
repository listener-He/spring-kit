package org.hehh.read;

import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * @author: HeHui
 * @date: 2020-07-20 17:11
 * @description: 本地缓存阅读数
 */
public class LocalCacheRead<ID> extends ReadAbstract<Double, ID> {


    private final ReadCache cache;

    /**
     * 是否有过读取（决定是否处理任务）
     */
    private final AtomicInteger isRead = new AtomicInteger(0);

    /**
     * 阅读文摘
     *
     * @param maxRead     最大阅读数
     * @param jobSeconds  定时秒
     * @param readStorage 读取存储
     */
    public LocalCacheRead(Double maxRead, int jobSeconds, ReadStorage<Double, ID> readStorage, String cacheKey) {
        super(maxRead, jobSeconds, readStorage);
        cache = new ReadCache(cacheKey);
    }

    /**
     * 执行
     */
    @Override
    protected void perform() {
        if (isRead.updateAndGet(r -> {
            return r > 0 ? -1 : 0;
        }) == -1) {
            super.perform();
        }
    }

    /**
     * 增加
     *
     * @param key 关键
     * @param n   阅读数
     *
     * @return {@link Double} 返回阅读数
     */
    @Override
    protected Optional<Double> increase(ID key, Double n) {
        isRead.updateAndGet(r -> {
            return r == -1 ? 1 : (r + 1);
        });

        Double aDouble = cache.get(key, Double.class);
        if (aDouble == null) {
            aDouble = 0D;
        }

        cache.put(key, aDouble + n);

        return Optional.ofNullable(aDouble + n);
    }


    /**
     * 减少
     *
     * @param key 关键
     * @param n   阅读数
     */
    @Override
    protected void reduce(ID key, Double n) {
        isRead.updateAndGet(r -> {
            return r == -1 ? 1 : (r + 1);
        });
        Double aDouble = cache.get(key, Double.class);
        if (aDouble != null) {
            if (aDouble <= n) {
                cache.evict(key);
                return;
            }
            cache.put(key, aDouble - n);
        }
    }


    /**
     * 得到所有
     *
     * @return {@link Map <ID, T>}
     */
    @Override
    protected Optional<Map<ID, Double>> getAll() {
        /**
         *  创建一个map把当前的数据放入此map中(如果不放进去，下面this.clear()清除时。数据就为空了)
         */
        ConcurrentMap<Object, Object> cacheMap = cache.getNativeCache();
        if (cacheMap == null || !cacheMap.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable((Map) new HashMap<>(cacheMap));
    }


    /**
     * 清除
     */
    @Override
    protected void clear() {
        cache.clear();
    }


    /**
     * 查询指定key的阅读数
     *
     * @param key 阅读key
     *
     * @return {@link Optional<Double>}
     */
    @Override
    public Optional<Double> getRead(ID key) {
        return Optional.ofNullable(cache.get(key, Double.class));
    }


    /**
     * 查询多个key的阅读数
     *
     * @param keys 阅读keys
     *
     * @return {@link Optional<Map<ID,Double>>}
     */
    @Override
    public Optional<Map<ID, Double>> getReads(List<ID> keys) {
        return Optional.ofNullable((Map) cache.getNativeCache().entrySet().stream().filter(v -> keys.contains(v.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }


    /**
     * 阅读室缓存
     */
    static class ReadCache extends ConcurrentMapCache {


        /**
         * Create a new ConcurrentMapCache with the specified name.
         *
         * @param name the name of the cache
         */
        public ReadCache(String name) {
            super(name, false);
        }


    }


}
