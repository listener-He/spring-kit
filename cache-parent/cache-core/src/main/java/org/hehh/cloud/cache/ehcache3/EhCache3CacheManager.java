package org.hehh.cloud.cache.ehcache3;

import org.ehcache.CacheManager;
import org.ehcache.Status;
import org.ehcache.config.CacheConfiguration;
import org.hehh.cloud.cache.CacheConfigurationParameter;
import org.hehh.cloud.cache.EhCache3Parameter;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-07-31 01:57
 * @description: ehcache3.0 缓存
 */
public class EhCache3CacheManager extends AbstractTransactionSupportingCacheManager {


    private final CacheManager cacheManager;

    private final String topicName;




    /**
     * @param parameter
     */
    public EhCache3CacheManager(CacheConfigurationParameter<EhCache3Parameter> parameter) {
        this.cacheManager = CacheConfigurationBuilders.builder(parameter);
        Assert.hasText(parameter.getTopicName(),"ehcache3.0 topicName不能为空");
        this.topicName = parameter.getTopicName();
    }


    /**
     * Load the initial caches for this cache manager.
     * <p>Called by {@link #afterPropertiesSet()} on startup.
     * The returned collection may be empty but must not be {@code null}.
     */
    @Override
    protected Collection<? extends Cache> loadCaches() {
        Assert.state(this.cacheManager != null, "No CacheManager set");

        Status status = cacheManager.getStatus();
        if (!Status.AVAILABLE.equals(status)) {
            throw new IllegalStateException(
                "An 'alive' EhCache CacheManager is required - current cache is " + status.toString());
        }

        Map<String, CacheConfiguration<?, ?>> cacheConfigurations = this.cacheManager.getRuntimeConfiguration().getCacheConfigurations();
        Collection<Cache> caches = new LinkedHashSet<>(cacheConfigurations.size());
        cacheConfigurations.forEach((k, v) -> {
            caches.add(new EhCache3Cache(this.cacheManager.getCache(k, v.getKeyType(), v.getValueType()), k, this.cacheManager.getStatus()));
        });

        return caches;
    }


    /**
     * 获取丢失的缓存
     *
     * @param name
     * @return
     */
    @Override
    protected Cache getMissingCache(String name) {
        Assert.state(cacheManager != null, "No CacheManager set");

        CacheConfiguration<?, ?> cacheConfiguration = cacheManager.getRuntimeConfiguration().getCacheConfigurations().get(name);
        if (cacheConfiguration != null) {
            return new EhCache3Cache(cacheManager.getCache(name, cacheConfiguration.getKeyType(), cacheConfiguration.getValueType()), name, cacheManager.getStatus());
        }
        return null;
    }








}
