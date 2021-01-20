package org.hehh.read;

import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-20 15:16
 * @description: redis缓存阅读
 */
public class RedisCacheRead<ID> extends ReadAbstract<Double, ID> {


    /**
     * redis操作类
     */
    private final RedisOperations<String, Double> redisOperations;


    /**
     * redis hash操作
     */
    private final BoundHashOperations<String, ID, Double> hash;


    /**
     * 缓存前缀
     */
    private final String cacheKey;


    /**
     * 搜索key拼接符
     */
    private final String scanKeyPrefix = "^";
    private final String scanKeySuffix = "$";
    private final String scanKeyJoin = "|";

    private final String value = "0";

    private final String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


    /**
     * 阅读文摘
     *
     * @param maxRead         最大阅读数
     * @param jobSeconds      定时秒
     * @param readStorage     读取存储
     * @param redisOperations redis操作类
     */
    protected RedisCacheRead(double maxRead, int jobSeconds, ReadStorage<Double, ID> readStorage, RedisOperations<String, Double> redisOperations) {
        this(maxRead, jobSeconds, readStorage, redisOperations, "read:cache:");
    }


    /**
     * 阅读文摘
     *
     * @param maxRead         最大阅读数
     * @param jobSeconds      定时秒
     * @param readStorage     读取存储
     * @param redisOperations redis操作类
     * @param cacheKey        缓存key
     */
    protected RedisCacheRead(double maxRead, int jobSeconds, ReadStorage<Double, ID> readStorage, RedisOperations<String, Double> redisOperations, String cacheKey) {
        super(maxRead, jobSeconds, readStorage);
        assert readStorage != null : "redis操作类不能为空";
        this.redisOperations = redisOperations;
        this.cacheKey = cacheKey;
        this.hash = redisOperations.boundHashOps(this.cacheKey);
    }


    /**
     * 执行（因redis可能为分布式部署，添加redis锁）
     */
    @Override
    protected void perform() {
        if (this.getLock(cacheKey + ":perform_job", (jobSeconds / 2) < 5 ? 5 : (jobSeconds / 2), TimeUnit.SECONDS)) {
            super.perform();
            /**
             *  释放锁
             */
            this.unLock(cacheKey + ":perform_job");
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
        return Optional.ofNullable(hash.increment(key, n));
    }


    /**
     * 减少
     *
     * @param key 关键
     * @param n   阅读数
     */
    @Override
    protected void reduce(ID key, Double n) {
        hash.increment(key, -n);
    }

    /**
     * 得到所有
     *
     * @return {@link Map <ID, T>}
     */
    @Override
    protected Optional<Map<ID, Double>> getAll() {
        return Optional.ofNullable(hash.entries());
    }


    /**
     * 清除
     */
    @Override
    protected void clear() {
        redisOperations.delete(cacheKey);
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
        return Optional.ofNullable(hash.get(key));
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

        /**
         *  搜索key
         */
        StringBuilder sb = new StringBuilder();

        /**
         *  拼接成正则
         */
        keys.forEach(v -> {
            sb.append(scanKeyPrefix).append(v.toString()).append(scanKeySuffix);
            if (keys.indexOf(v) < (keys.size() - 1)) {
                sb.append(scanKeyJoin);
            }
        });


        /**
         *  搜索
         */
        Cursor<Map.Entry<ID, Double>> cursor = hash.scan(new ScanOptions.ScanOptionsBuilder().match(sb.toString()).count(keys.size()).build());

        Map<ID, Double> map = new HashMap<>(keys.size());
        while (cursor.hasNext()) {
            Map.Entry<ID, Double> next = cursor.next();
            map.put(next.getKey(), next.getValue());
        }

        return Optional.ofNullable(map);
    }


    /**
     * 获取锁
     *
     * @param key
     * @param timeout
     * @param timeUnit
     *
     * @return
     */
    private boolean getLock(String key, long timeout, TimeUnit timeUnit) {
        try {

            return redisOperations.execute((RedisCallback<Boolean>) connection ->
                connection.set(key.getBytes(Charset.forName("UTF-8")), value.getBytes(Charset.forName("UTF-8")),
                    Expiration.from(timeout, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));

        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 释放锁
     *
     * @param key
     */
    private void unLock(String key) {
        try {

            Boolean unLockStat = redisOperations.execute((RedisCallback<Boolean>) connection ->
                connection.eval(script.getBytes(), ReturnType.BOOLEAN, 1,
                    key.getBytes(Charset.forName("UTF-8")), value.getBytes(Charset.forName("UTF-8"))));
            if (!unLockStat) {
            }
        } catch (Exception e) {
        }
    }
}
