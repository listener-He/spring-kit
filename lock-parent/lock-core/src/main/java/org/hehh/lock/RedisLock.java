package org.hehh.lock;

import lombok.extern.slf4j.Slf4j;
import org.hehh.lock.exception.LockException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-27 22:43
 * @description: redis实现锁
 */
@Slf4j
public class RedisLock implements ILock {


    /**
     *  redis操作类
     */
    private final RedisOperations<String,Object> redisOperations;

    /**
     *  前缀
     */
    private String prefix;

    private  final String value = "0";

    private final String script;

    public RedisLock(RedisOperations<String, Object> redisOperations) {
        this(redisOperations,null);
    }


    public RedisLock(RedisOperations<String, Object> redisOperations,String prefix) {
        this(redisOperations,prefix,"if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
    }


    public RedisLock(RedisOperations<String, Object> redisOperations,String prefix,String script) {
        assert redisOperations != null : "redis-lock 操作类不能为空";
        this.redisOperations = redisOperations;
        this.prefix = prefix;
        Assert.hasText(script,"redis-lock 脚本不能为空");
        this.script = script;
    }


    /**
     * 最终的路径
     *
     * @param path 路径
     * @return {@link String}
     */
    private String finalPath(String path){
        Assert.hasText(path,"分布式锁路径不能为空");

        if(StringUtils.isEmpty(prefix)){
            return path;
        }
        if (!path.startsWith(":")) {
            path = ":"+path;
        }
        return prefix + path;
    }



    /**
     * 互斥锁
     *
     * @param key      关键
     * @param time     时间
     * @param timeUnit 时间单位
     */
    @Override
    public void mutex(String key, long time, TimeUnit timeUnit) throws LockException {
        getLock(finalPath(key), time, timeUnit);
    }


    /**
     * 互斥锁
     *
     * @param key      关键
     * @param time     时间
     * @param timeUnit 时间单位
     * @param callback 回调
     * @return {@link Optional <T> }* @throws LockException 锁例外
     */
    @Override
    public <T> Optional<T> mutex(String key, long time, TimeUnit timeUnit, LockCallback<T> callback) throws LockException {
        getLock(finalPath(key), time, timeUnit);
        return Optional.ofNullable(callback.doInLock());
    }


    /**
     * 释放锁
     *
     * @param key
     */
    @Override
    public void release(String key) {
        unLock(finalPath(key));
    }



    /**
     *  获取锁
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    private void getLock(String key, long timeout, TimeUnit timeUnit) throws LockException {
        try {

            redisOperations.execute((RedisCallback< Boolean>) connection ->
                connection.set(key.getBytes(Charset.forName("UTF-8")), value.getBytes(Charset.forName("UTF-8")),
                    Expiration.from(timeout, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));
        } catch (Exception e) {
            throw new LockException("获取分布式锁失败，key="+key,e);
        }
    }



    /**
     *  释放锁
     * @param key
     */
    private void unLock(String key) {
        try {

            Boolean unLockStat = redisOperations.execute((RedisCallback<Boolean>) connection ->
                connection.eval(script.getBytes(), ReturnType.BOOLEAN, 1,
                    key.getBytes(Charset.forName("UTF-8")), value.getBytes(Charset.forName("UTF-8"))));
            if (!unLockStat) {
                log.error("释放分布式锁失败，key={}，已自动超时，其他线程可能已经重新获取锁", key);
            }
        } catch (Exception e) {
            log.error("释放分布式锁失败，key={}", key, e);
        }
    }
}
