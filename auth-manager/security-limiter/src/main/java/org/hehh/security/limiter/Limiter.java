package org.hehh.security.limiter;

import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-12 16:51
 * @description: 限流器
 */
public interface Limiter<T> {





    /**
     *  堵塞请求限流
     *     该方法会被阻塞直到获取到请求
     * @param callback 回调
     * @return
     */
    T acquire(LimiterCallback<T> callback) throws LimiterException;





    /**
     *  请求限流
     *    获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回
     * @param timeout 超时时间
     * @param unit 超时单位
     * @param callback 回调
     * @return
     */
    T acquire(long timeout, TimeUnit unit,LimiterCallback<T> callback) throws LimiterException;




    /**
     *  试一下请求限流（如果没有马上报错）
     * @param callback 回调
     * @return
     * @throws LimiterException
     */
    default T tryAcquire(LimiterCallback<T> callback) throws LimiterException {
        return this.acquire(0,TimeUnit.MILLISECONDS,callback);
    }

}
