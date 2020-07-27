package org.hehh.lock;

import org.hehh.lock.exception.LockException;

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
    void mutex(String key,long time, TimeUnit timeUnit) throws LockException;


    /**
     * 互斥锁
     *
     * @param key      关键
     * @param time     时间
     * @param timeUnit 时间单位
     * @param callback 回调
     * @return {@link T}* @throws LockException 锁例外
     */
    <T> T mutex(String key,long time, TimeUnit timeUnit,LockCallback<T> callback) throws LockException;



    /**
     *  释放锁
     * @param key
     */
    void release(String key);


}
