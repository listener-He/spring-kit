package org.hehh.security.limiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author: HeHui
 * @date: 2020-07-12 16:54
 * @description: 基于谷歌的令牌限流器 中文文档: https://ifeve.com/guava-ratelimiter/
 */
@SuppressWarnings("ALL")
public class GuavaRateLimiter<T> implements PermitsLimiter<T> {

    private final int permits;

    private final RateLimiter limiter;


    /**
     *  使用 SmoothBursty 平滑突发性限流
     * @see SmoothRateLimiter.SmoothBursty
     * @param permitsPerSecond 每秒增加多少令牌-QPS
     * @param permits 每次消耗多少个许可
     */
    public GuavaRateLimiter(double permitsPerSecond, int permits){
        this.limiter = RateLimiter.create( permitsPerSecond);
        this.permits = permits;
    }



    /**
     *  使用 SmoothWarmingUp 平滑预热限流
     *    SleepingStopWatch是一个可sleep的秒表，起始时间是构建StopWatch的时间，sleepMicrosUninterruptibly方法支持不受中断的sleep，sleep是当前线程的sleep。
     * @see SmoothRateLimiter.SmoothWarmingUp
     * @param permitsPerSecond 每秒增加多少令牌-QPS
     * @param warmupPeriod 预热期时间
     * @param unit 预热期时间单位
     * @param permits 每次消耗多少许可
     */
    public GuavaRateLimiter(double permitsPerSecond, long warmupPeriod, TimeUnit unit,int permits){
        this.limiter = RateLimiter.create(permitsPerSecond,warmupPeriod ,unit );
        this.permits = permits;
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
        double acquire = limiter.acquire(permits);
        if(acquire > 0){
            return callback.through(acquire);
        }else{
            throw new LimiterException("获取不到资源",permits);
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
     * 获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回false
     *
     * @param timeout  超时时间
     * @param unit     超时单位
     * @param callback 回调
     * @return
     */
    @Override
    public T acquire(long timeout, TimeUnit unit, LimiterCallback<T> callback) throws LimiterException {
      return  this.acquire(permits,timeout,unit,callback);
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
        long millis = System.currentTimeMillis();
        boolean acquire = limiter.tryAcquire(permits, timeout, unit);
        if(acquire){
           return callback.through(System.currentTimeMillis() - millis / SECONDS.toMicros(1L));
        }else{
            throw new LimiterException("获取不到资源",permits);
        }
    }



}
