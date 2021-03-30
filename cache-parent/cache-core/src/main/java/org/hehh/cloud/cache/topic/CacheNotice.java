package org.hehh.cloud.cache.topic;

import lombok.Data;

/**
 * @author  HeHui
 * @create: 2019-09-27 10:49
 * @description 缓存通知
 **/
@Data
public class CacheNotice implements java.io.Serializable {

    /**
     *  通知类型
     */
    private NoticeType type;

    /**
     * 缓存key
     */
    private Object key;

    /**
     * 缓存名称
     */
    private String cacheName;


    private int level;


    /**
     * 创建
     *
     * @param type      类型
     * @param key       关键
     * @param cacheName 缓存名称
     * @return {@link CacheNotice}
     */
    public static CacheNotice create(NoticeType type,Object key,String cacheName,int level){
        CacheNotice cacheNotice = new CacheNotice();
        cacheNotice.setType(type);
        cacheNotice.setKey(key);
        cacheNotice.setCacheName(cacheName);
        cacheNotice.setLevel(level);
        return cacheNotice;
    }
}
