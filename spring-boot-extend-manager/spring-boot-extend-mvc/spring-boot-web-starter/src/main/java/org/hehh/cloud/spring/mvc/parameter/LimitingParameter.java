package org.hehh.cloud.spring.mvc.parameter;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-26 17:21
 * @description: 限流参数
 */
@Data
public class LimitingParameter {

    private int permits = 1;

    private long timeout = 3;

    private TimeUnit unit = TimeUnit.SECONDS;

    private LimitingType type = LimitingType.RATE;

    private Rate rate;

    private Semaphore semaphore;

    /**
     *  令牌桶
     */
    @Data
    public static class Rate{

        /**
         * 每秒增加多少令牌-QPS
         */
        private double permitsPerSecond;

        /**
         * 预热期时间
         */
        private Long warMupPeriod;


        /**
         * 预热期时间单位
         */
        private TimeUnit unit;


    }


    /**
     *  信号量
     */
    @Data
    public static class Semaphore{

        /**
         *  总数
         */
        private int capacity;

        /**
         *  最大等待数
         */
        private int maxQueueLength = Integer.MAX_VALUE;
    }

}
