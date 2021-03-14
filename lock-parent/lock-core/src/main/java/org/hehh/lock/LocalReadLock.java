package org.hehh.lock;

import org.hehh.lock.exception.LockException;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: HeHui
 * @date: 2020-07-27 23:14
 * @description: 本地读锁 (只适用于固定的key值的锁场景，且不不能用于分布式环境中)
 */
public class LocalReadLock implements ILock {


    private final Map<String, ReentrantReadWriteLock> lockMap = new ConcurrentHashMap<>(128);


    private final Lock thisLock;


    private final int jobSeconds;


    public LocalReadLock() {
        this(new ReentrantReadWriteLock().readLock(), 60);
    }

    public LocalReadLock(Lock thisLock, int jobSeconds) {
        this.thisLock = thisLock;
        this.jobSeconds = jobSeconds;
    }


    /**
     * init job task
     */
    private void initJob() {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleWithFixedDelay(this::remove, 0, jobSeconds, TimeUnit.SECONDS);
    }


    private void remove() {
        try {
            thisLock.lock();

            Iterator<String> iterator = lockMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                ReentrantReadWriteLock lock = lockMap.get(key);
                if (lock == null || !lock.hasQueuedThreads()) {
                    iterator.remove();
                }
            }
            thisLock.unlock();
        } catch (Exception e) {
        }

    }


    private Lock getLock(String key, long time, TimeUnit timeUnit) throws InterruptedException {
        if (thisLock.tryLock(time, timeUnit)) {
            try {
                ReentrantReadWriteLock reentrantReadWriteLock = lockMap.get(key);
                if (reentrantReadWriteLock == null) {
                    reentrantReadWriteLock = new ReentrantReadWriteLock();
                    lockMap.put(key,reentrantReadWriteLock);
                }
                return reentrantReadWriteLock.readLock();
            } finally {
                thisLock.unlock();
            }
        }
        return null;
    }

    private <T> T getLock(String key,long time, TimeUnit timeUnit,boolean isNew,LockCallback<T> callback) throws Throwable {
        if(thisLock.tryLock(time,timeUnit)){
            try {
                ReentrantReadWriteLock reentrantReadWriteLock = lockMap.get(key);
                if(reentrantReadWriteLock == null && isNew){
                    reentrantReadWriteLock = new ReentrantReadWriteLock();
                    lockMap.put(key,reentrantReadWriteLock);
                }
                if (reentrantReadWriteLock == null || !reentrantReadWriteLock.readLock().tryLock(time,timeUnit)) {
                    throw new LockException("未能获取到锁:"+key);
                }
                reentrantReadWriteLock.readLock().lock();
                if ( callback != null ){
                    callback.doInLock();
                    reentrantReadWriteLock.readLock().unlock();
                    lockMap.remove(key);
                }
            }finally {
                thisLock.unlock();
            }
        }
        return null;
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
        try {
            Lock lock = getLock(key, time, timeUnit);
            if (lock == null || !lock.tryLock(time, timeUnit)) {
                throw new LockException(String.format("未能获取到锁:s%", key));
            }
        } catch (InterruptedException e) {
            throw new LockException(String.format("未能获取到锁:s%", key), e);
        }
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
    public <T> Optional<T> mutex(String key, long time, TimeUnit timeUnit, LockCallback<T> callback) throws Throwable {

        try {
            return Optional.ofNullable(getLock(key, time, timeUnit,true,callback));
//            if(lock == null || !lock.tryLock(time,timeUnit)){
//                throw new LockException("未能获取到锁:"+key);
//            }
           // return Optional.ofNullable(callback.doInLock());
        } catch (InterruptedException e) {
            throw new LockException("未能获取到锁:"+key,e);
        }
    }





    /**
     * 释放锁
     *
     * @param key
     */
    @Override
    public void release(String key) {
        try {
            Lock lock = getLock(key, 10, TimeUnit.MILLISECONDS,false,null);
            if (lock != null) {
                lock.unlock();
                lockMap.remove(key);
            }
        } catch (Throwable t) {
        }
    }


}
