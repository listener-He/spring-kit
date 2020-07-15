package org.hehh.security.limiter;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: HeHui
 * @date: 2020-07-15 11:42
 * @description: 滑动窗口限流
 */
public class SlidingWindowsLimiter {

    /**
     * 时间窗口
     **/
    private final long window;
    /**
     * 窗口的size 用于计算总的流量上限
     **/
    private final int maxWindow;

    /**
     * 原子计数
     **/
    private final AtomicInteger count;

    /**
     * 时间队列
     */
    private Deque<Long> timeQueue;


    public SlidingWindowsLimiter(long window, int maxWindow) {
        this.window = window;
        this.maxWindow = maxWindow;
        this.count = new AtomicInteger(0);
        timeQueue.addFirst(System.currentTimeMillis());
    }


    /**
     * 流量限制
     **/
    public Boolean tryout(long now, int permits) {
        if ((now - timeQueue.peekLast()) > window) {
            timeQueue.clear();
            timeQueue.addFirst(now);
            count.set(0);
        } else {
            if (count.addAndGet(permits) < maxWindow) {
                timeQueue.add(now);
                return true;
            }

        }
        return Boolean.FALSE;
    }






    /**
     * 滑动时间窗口限流实现
     * 假设某个服务最多只能每秒钟处理100个请求，我们可以设置一个1秒钟的滑动时间窗口，
     * 窗口中有10个格子，每个格子100毫秒，每100毫秒移动一次，每次移动都需要记录当前服务请求的次数
     */
    static class SlidingWindow{
        /**
         * 时间窗口内最大请求数
         */
        public final int limit;
        /**
         * 服务访问次数
         */
        private  long counter = 0L;

        /**
         * 使用LinkedList来记录滑动窗口的10个格子
         */
        private final LinkedList<Long> slots = new LinkedList<>();
        /**
         * 时间划分多少段落
         */
        private final int split;


        SlidingWindow(int limit, int split) {
            this.limit = limit;
            this.split = split;
        }


        private boolean doCheck() throws InterruptedException {
            while (true) {
                slots.addLast(counter);
                if (slots.size() > split) {
                    slots.removeFirst();// 超出了，就把第一个移出。
                }

                // 比较最后一个和第一个，两者相差100以上就限流
                if ((slots.peekLast() - slots.peekFirst()) > limit) {
                    return true;
                } else {
                    return false;
                }
            }
        }

    }



}

    


    