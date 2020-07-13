package org.hehh.security.limiter;

/**
 * @author: HeHui
 * @date: 2020-07-13 15:18
 * @description: 限流回调
 */
public interface LimiterCallback<T> {


    /**
     *  限流通过
     * @return
     */
     T through();
}
