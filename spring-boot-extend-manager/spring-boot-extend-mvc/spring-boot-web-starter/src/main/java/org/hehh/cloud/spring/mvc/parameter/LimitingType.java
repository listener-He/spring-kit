package org.hehh.cloud.spring.mvc.parameter;

/**
 * @author: HeHui
 * @date: 2020-07-26 17:26
 * @description: 限流类型
 */
public enum LimitingType {

    /**
     *  令牌桶
     */
    RATE,
    /**
     *  信号量
     */
    SEMAPHORE
}
