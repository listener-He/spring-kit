package org.hehh.read;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: HeHui
 * @date: 2020-07-20 14:44
 * @description: 抽象阅读
 */
public abstract class ReadAbstract<T extends Number,ID>  implements Read<T,ID> {


    /**
     * 最大读取值，当达到此读取值时。数据入库
     */
    private final T maxRead;

    /**
     * 定时多少秒 入库一次
     */
    protected final int jobSeconds;


    /**
     *  阅读数存储
     */
    protected final ReadStorage<T,ID> readStorage;


    /**
     *  是否有过读取（决定是否处理任务）
     */
    private final AtomicInteger isRead = new AtomicInteger(0);



    /**
     * 阅读文摘
     *
     * @param maxRead     最大阅读数
     * @param jobSeconds  定时秒
     * @param readStorage 读取存储
     */
    protected ReadAbstract(T maxRead, int jobSeconds, ReadStorage<T, ID> readStorage) {
        assert readStorage != null : "ReadStorage不能位空";
        assert maxRead != null  : "最大阅读数不能位空";
        assert maxRead.doubleValue() > 0.01D  : "最大阅读数不能小于0.01";
        assert jobSeconds > 60  : "定时秒不能小于60";

        this.maxRead = maxRead;
        this.jobSeconds = jobSeconds;
        this.readStorage = readStorage;

        this.initJob();
    }


    /**
     *  初始化job
     */
    private void initJob(){
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleWithFixedDelay(this::perform,0,jobSeconds, TimeUnit.SECONDS);
    }




    /**
     *  执行
     */
    protected void perform(){
        if(isRead.get() > 0){
            this.getAll().ifPresent(data->{
                try {
                    readStorage.increase(data);
                    this.clear();
                }catch (Exception e){
                    //TODO if exception then ?
                }
            });
            isRead.getAndSet(0);
        }

    }






    /**
     * 读取
     *
     * @param key      阅读的key
     * @param n        增加数
     * @param clientID 客户端ID （非必填）
     */
    @Override
    public void read(ID key, T n, String clientID) {
        //TODO if clientID not null ?
        isRead.incrementAndGet();

        this.increase(key, n).ifPresent(d -> {
            /**
             *  达到最大数条件
             */
            if(d.doubleValue() >= maxRead.doubleValue()){
                readStorage.increase(key,d);
                /**
                 *  缓存中马上减少对应数
                 */
                this.reduce(key,d);
            }
        });

    }




    /**
     * 增加
     *
     * @param key 关键
     * @param n   阅读数
     * @return {@link T} 返回阅读数
     */
    protected abstract Optional<T> increase(ID key, T n);



    /**
     * 减少
     *
     * @param key 关键
     * @param n   阅读数
     */
    protected abstract void reduce(ID key,T n);


    /**
     * 得到所有
     *
     * @return {@link Map<ID, T>}
     */
    protected abstract Optional<Map<ID,T>> getAll();


    /**
     * 清除
     */
    protected abstract void clear();
}
