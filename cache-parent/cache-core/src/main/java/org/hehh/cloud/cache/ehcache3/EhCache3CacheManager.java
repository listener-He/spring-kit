package org.hehh.cloud.cache.ehcache3;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;

/**
 * @author: HeHui
 * @date: 2020-07-31 01:57
 * @description: ehcache3.0 缓存
 */
public class EhCache3CacheManager extends AbstractTransactionSupportingCacheManager {



    /**
     * Load the initial caches for this cache manager.
     * <p>Called by {@link #afterPropertiesSet()} on startup.
     * The returned collection may be empty but must not be {@code null}.
     */
    @Override
    protected Collection<? extends Cache> loadCaches() {
        return null;
    }






}
