package org.hehh.security.limiter;

import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-12 16:51
 * @description: 限流器
 */
public interface Limiter {


    /**
     *  堵塞请求限流
     *     该方法会被阻塞直到获取到请求
     * @return
     */
    boolean acquire();


    /**
     *  请求限流
     *    获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回false
     * @return
     */
    boolean tryAcquire();



    /**
     *  请求限流
     *    获取指定许可数如果该许可数可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可数的话，那么立即返回false
     * @param timeout 超时时间
     * @param unit 超时单位
     * @return
     */
    boolean tryAcquire(long timeout, TimeUnit unit);



}
