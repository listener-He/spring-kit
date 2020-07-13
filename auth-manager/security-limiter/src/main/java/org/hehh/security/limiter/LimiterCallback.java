package org.hehh.security.limiter;

/**
 * @author: HeHui
 * @date: 2020-07-13 15:18
 * @description: 限流回调
 */
public interface LimiterCallback<T> {


    /**
     * 通过
     *
     * @param latencyTime 延迟时间（秒）
     * @return {@link T}
     */
    T through(double latencyTime);
}
