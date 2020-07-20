package org.hehh.read;

import cn.hutool.core.thread.ThreadUtil;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author: HeHui
 * @date: 2020-07-20 17:11
 * @description: 本地缓存阅读数
 */
public class LocalCacheRead<ID> extends ReadAbstract<Double,ID>{


    private ReadCache cache;



    /**
     * 阅读文摘
     *
     * @param maxRead     最大阅读数
     * @param jobSeconds  定时秒
     * @param readStorage 读取存储
     */
    public LocalCacheRead(Double maxRead, int jobSeconds, ReadStorage<Double, ID> readStorage,String cacheKey) {
        super(maxRead, jobSeconds, readStorage);
        cache = new ReadCache(cacheKey);
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
        return Optional.ofNullable((Map) cache.getNativeCache());
    }


    /**
     * 清除
     */
    @Override
    void clear() {
        cache.clear();
    }


    /**
     * 查询指定key的阅读数
     *
     * @param key 阅读key
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
     * @return {@link Optional<Map<ID,Double>>}
     */
    @Override
    public Optional<Map<ID, Double>> getReads(List<ID> keys) {
        return Optional.ofNullable((Map)cache.getNativeCache().entrySet().stream().filter(v->keys.contains(v.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }








    /**
     *  阅读室缓存
     */
    static class ReadCache extends ConcurrentMapCache{


        /**
         * Create a new ConcurrentMapCache with the specified name.
         *
         * @param name the name of the cache
         */
        public ReadCache(String name) {
            super(name,false);
        }


    }


//    public static void main(String[] args) {
//        Read<Double,Integer> read = new LocalCacheRead<Integer>(10D, 61, new ReadStorage<Double, Integer>() {
//            @Override
//            public void increase(Integer key, Double n) {
//                System.out.println("指定");
//            }
//
//            @Override
//            public void increase(Map<Integer, Double> data) {
//                System.out.println("定时"+data.values().toString());
//            }
//        },"read:test");
//
//        read.read(1,1D,null);
//        read.read(1,1D,null);
//
//        ThreadUtil.sleep(60000);
//
//        read.read(1,1D,null);
//
//    }
}
