package org.hehh.security.limiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-12 16:54
 * @description: 基于谷歌的令牌限流器 中文文档: https://ifeve.com/guava-ratelimiter/
 */
public class GuavaRateLimiter implements Limiter {

    private  long max;

    private final int permits;

    private final RateLimiter limiter;


    public GuavaRateLimiter(double permitsPerSecond, long warmupPeriod, TimeUnit unit,int permits){
        this.limiter = RateLimiter.create(permitsPerSecond,warmupPeriod ,unit );
        this.permits = permits;
    }


    /**
     * 堵塞请求限流
     * 该方法会被阻塞直到获取到请求
     *
     * @return
     */
    @Override
    public boolean acquire() {
        double acquire = limiter.acquire(permits);

        return false;
    }

    /**
     * 请求限流
     * 获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回false
     *
     * @return
     */
    @Override
    public boolean tryAcquire() {
        return false;
    }

    /**
     * 请求限流
     * 获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回false
     *
     * @param timeout 超时时间
     * @param unit    超时单位
     * @return
     */
    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return false;
    }
}
