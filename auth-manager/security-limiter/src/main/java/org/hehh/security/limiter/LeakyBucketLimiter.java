package org.hehh.security.limiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author: HeHui
 * @date: 2020-07-14 22:23
 * @description: 漏桶算法限流
 */
public class LeakyBucketLimiter<T> implements Limiter<T> {


    /**
     *  每次默认消耗许可
     */
    private final int permits;

    /**
     * 桶的容量
     */
    private final int capacity;

    /**
     * 木桶剩余的水滴的量(初始化的时候的空的桶)
     */
    private final AtomicInteger water = new AtomicInteger(0);
    /**
     * 水滴的流出的速率 每1000毫秒流出滴
     */
    private final int leakRate;

    /**
     * 第一次请求之后,木桶在这个时间点开始漏水
     */
    private long leakTimeStamp;


    /**
     * 漏水的桶限幅器
     *
     * @param capacity 能力
     * @param leakRate 泄漏率
     * @param permits  许可证
     */
    public LeakyBucketLimiter(int capacity,int leakRate, int permits) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.permits = permits;
        this.leakTimeStamp = System.currentTimeMillis();
    }


    /**
     * 补水
     */
    private void makeSpace(long now) {
        long time = now - leakTimeStamp;
        int leaked = Long.valueOf(time * leakRate / capacity).intValue();
        if (leaked < 1) {
            return;
        }

        if (water.get() + leaked > capacity) {
            water.set(capacity);
        }else{
            water.getAndAdd(leaked);
        }
        leakTimeStamp = now;
    }


    /**
     * 漏水。桶里水量不够就返回false
     * @param quota 漏水量
     * @return 是否漏水成功
     */
    private boolean tryWatering(int quota,long now) {
        makeSpace(now);
        int left = water.get() -quota ;
        if (left >= 0) {
            water.set(left);
            return true;
        }
        return false;
    }



    /**
     * 漏水。没水就阻塞直到蓄满足够的水
     * @param quota 要漏的数量
     */
    private boolean watering(int quota,long now,Long endTime) {
        int left;
        try {
            do {
                makeSpace(now);
                left = water.get() - quota;
                if (left >= 0) {
                    water.getAndAdd(-left);
                    return true;
                } else {
                    Thread.sleep(10);
                }
            } while (left < 0 || (endTime != null && System.currentTimeMillis() >= endTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
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
        long millis = System.currentTimeMillis();
        if(this.watering(permits,millis,null)){
             return callback.through(System.currentTimeMillis() - millis / SECONDS.toMicros(1L));
        }

        throw new LimiterException("获取不到资源",permits);
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
        long millis = System.currentTimeMillis();
        if(timeout <= 0){
            if(this.tryWatering(permits,millis)){
               return  callback.through(System.currentTimeMillis() - millis / SECONDS.toMicros(1L));
            }
        }else{
            long endTime = TimeUnit.MILLISECONDS.convert(timeout, unit);
            if(this.watering(permits,millis,millis  +endTime)){
                return  callback.through(System.currentTimeMillis() - millis / SECONDS.toMicros(1L));
            }
        }
        throw new LimiterException("获取不到资源",permits);
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


    public static void main(String[] args) throws LimiterException {
        Limiter<Double> limiter = new LeakyBucketLimiter<>(10,1,1);

        int a = 100;
        for (int i = 0; i < a; i++) {
            try {
                Double acquire = limiter.acquire(3000, TimeUnit.MILLISECONDS, (x) -> {
                    return x;
                });
                System.out.println("第"+(i+1)+"次成功"+acquire);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
