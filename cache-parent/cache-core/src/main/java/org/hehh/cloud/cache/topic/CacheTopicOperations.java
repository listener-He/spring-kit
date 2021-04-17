package org.hehh.cloud.cache.topic;

/**
 * @author  HeHui
 * @date  2020-07-31 15:13
 * @description 缓存topic操作
 */
public interface CacheTopicOperations<T extends CacheNotice> {


    /**
     *  发送
     * @param topic 主题
     * @param notice 数据
     */
    void send(String topic, T notice);

}
