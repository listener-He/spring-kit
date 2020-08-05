package org.hehh.cloud.cache;

import org.springframework.cache.CacheManager;

import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-07-31 16:20
 * @description: 多层缓存管理
 */
public interface MoreCacheManager extends CacheManager {

    /**
     * 主题名称
     *
     * @return {@link String}
     */
    String topicName();


    /**
     * 获取缓存管理器
     *
     * @param level 层级
     * @return {@link Optional<CacheManager>}
     */
    Optional<CacheManager> getCacheManager(int level);

}
