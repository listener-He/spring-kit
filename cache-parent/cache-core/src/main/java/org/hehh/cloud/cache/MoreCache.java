package org.hehh.cloud.cache;

import org.springframework.cache.Cache;

import java.util.Optional;

/**
 * @author  HeHui
 * @date  2020-07-31 11:47
 * @description 多级缓存
 */
public interface MoreCache extends Cache {

    /**
     * 缓存名
     *
     * @return {@link String}
     */
    String getName();


    /**
     * 主题名称
     *
     * @return {@link String}
     */
    String topicName();


    /**
     * 得到缓存
     * @param level 级别
     * @return {@link Optional<Cache>}
     */
    Optional<Cache> getCache(int level);



}
