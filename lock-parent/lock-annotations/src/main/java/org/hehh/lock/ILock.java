package org.hehh.lock;

import org.hehh.lock.exception.LockException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-27 22:34
 * @description: 锁接口
 */
public interface ILock {

    /**
     * 互斥锁
     *
     * @param key      关键
     * @param time     时间
     * @param timeUnit 时间单位
     */
    void mutex(String key, long time, TimeUnit timeUnit) throws LockException;


    /**
     * 互斥锁
     *
     * @param key      关键
     * @param time     时间
     * @param timeUnit 时间单位
     * @param callback 回调
     *
     * @return {@link Optional<T> }
     *
     * @throws LockException 锁例外
     */
    default <T> Optional<T> mutex(String key, long time, TimeUnit timeUnit, LockCallback<T> callback) throws Throwable {
        mutex(key, time, timeUnit);
        try {
            return Optional.ofNullable(callback.doInLock());
        } finally {
            //TODO 是否主动释放锁 ？
            //release(key);
        }

    }


    /**
     * 释放锁
     *
     * @param key
     */
    void release(String key);


}
