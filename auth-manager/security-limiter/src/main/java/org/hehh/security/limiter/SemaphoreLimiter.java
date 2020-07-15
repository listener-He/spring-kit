package org.hehh.security.limiter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-15 10:51
 * @description: 信号量限流
 */
public class SemaphoreLimiter<T> implements PermitsLimiter<T> {


    /**
     *  信号量（最大许可）
     */
    private final Semaphore semaphore;


    /**
     *  每次默认消耗许可
     */
    private final int permits;

    /**
     *  最大等待许可数量
     */
    private final int maxQueueLength;

    /**
     * 信号限幅器
     *  @param capacity 能力
     * @param permits  许可证
     * @param maxQueueLength
     */
    public SemaphoreLimiter(int capacity, int permits, int maxQueueLength) {
        this.semaphore = new Semaphore(capacity,true);
        this.permits = permits;
        this.maxQueueLength = maxQueueLength;
    }


    /**
     * 信号限幅器
     *
     * @param capacity 能力
     * @param permits  许可证
     */
    public SemaphoreLimiter(int capacity, int permits) {
        this(capacity,permits,Integer.MAX_VALUE);
    }




    /**
     * 堵塞请求限流
     * 该方法会被阻塞直到获取到请求
     *
     * @param permits  许可证
     * @param callback 回调
     * @throws LimiterException 限幅器异常
     */
    @Override
    public T acquire(int permits, LimiterCallback<T> callback) throws LimiterException {

        if(semaphore.getQueueLength() >= maxQueueLength){
            throw new LimiterException("当前等待队列请求过多,MAX"+maxQueueLength,permits);
        }
        try {
            long millis = System.currentTimeMillis();
            semaphore.acquire(permits);
            try {
                T t = callback.through((System.currentTimeMillis() - millis) / 1000);
                return t;
            }finally {
                semaphore.release(permits);
            }

        } catch (InterruptedException e) {
            throw new LimiterException("获取资源中断",permits,e);
        }
    }


    /**
     * 堵塞请求限流
     * 该方法会被阻塞直到获取到请求
     *
     * @param callback 回调
     * @return
     */
    @Override
    public T acquire(LimiterCallback<T> callback) throws LimiterException {
        return this.acquire(permits,callback);
    }

    /**
     * 请求限流
     * 获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回
     *
     * @param permits  许可证
     * @param timeout  超时时间
     * @param unit     超时单位
     * @param callback 回调
     * @return
     */
    @Override
    public T acquire(int permits, long timeout, TimeUnit unit, LimiterCallback<T> callback) throws LimiterException {
        if(semaphore.getQueueLength() >= maxQueueLength){
            throw new LimiterException("当前等待队列请求过多,MAX"+maxQueueLength,permits);
        }


        try {

            long millis = System.currentTimeMillis();
            if(semaphore.tryAcquire(permits,timeout,unit)){
                try {
                    T t = callback.through((System.currentTimeMillis() - millis) / 1000);
                    return t;
                }finally {
                    semaphore.release(permits);
                }
            }
        } catch (InterruptedException e) {
            throw new LimiterException("获取资源中断",permits,e);
        }

        throw new LimiterException("获取到资源",permits);
    }



    /**
     * 请求限流
     * 获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回
     *
     * @param timeout  超时时间
     * @param unit     超时单位
     * @param callback 回调
     * @return
     */
    @Override
    public T acquire(long timeout, TimeUnit unit, LimiterCallback<T> callback) throws LimiterException {
        return this.acquire(permits,timeout,unit,callback);
    }



}
