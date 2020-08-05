package org.hehh.security.limiter;

import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-15 14:55
 * @description: 许可限流
 */
public interface PermitsLimiter<T> extends Limiter<T> {

    /**
     * 堵塞请求限流
     *    该方法会被阻塞直到获取到请求
     *
     * @param permits  许可证
     * @param callback 回调
     * @throws LimiterException 限幅器异常
     */
    T acquire(int permits,LimiterCallback<T> callback) throws LimiterException;



    /**
     *  请求限流
     *    获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回
     * @param permits 许可证
     * @param timeout 超时时间
     * @param unit 超时单位
     * @param callback 回调
     * @return
     */
    T acquire(int permits, long timeout, TimeUnit unit, LimiterCallback<T> callback) throws LimiterException;
}
