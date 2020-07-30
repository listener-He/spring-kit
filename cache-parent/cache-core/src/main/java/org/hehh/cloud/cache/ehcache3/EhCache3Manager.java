package org.hehh.cloud.cache.ehcache3;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.StateTransitionException;
import org.ehcache.Status;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.core.EhcacheManager;
import org.ehcache.impl.persistence.DefaultDiskResourceService;
import org.ehcache.spi.service.Service;
import org.ehcache.xml.XmlConfiguration;
import org.hehh.cloud.cache.CacheConfigurationParameter;
import org.hehh.cloud.cache.EhCache3Parameter;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * @author: HeHui
 * @date: 2020-07-31 01:59
 * @description: ehcache3.0 缓存实现
 */
@Slf4j
public class EhCache3Manager  implements CacheManager  {


    private String topicName;

    private CacheManager cacheManager;

    /**
     * 嗯cache3经理
     *
     * @param parameter 参数
     */
    public EhCache3Manager(CacheConfigurationParameter<EhCache3Parameter> parameter){
        assert parameter != null : "ehcache3.0 配置参数不能为空";
        this.topicName = parameter.getTopicName();


        if(StringUtils.isEmpty(parameter.getLoadingFile())){
            /**
             *  缓存管理器构造
             */
            CacheManagerBuilder cacheManagerBuilder = CacheManagerBuilder.newCacheManagerBuilder();

            if ((null != parameter.getCaches() && parameter.getCaches().size() > 0) ? parameter.getCaches().stream().anyMatch(v -> v.getDiskEnable()) : false) {
                /**
                 *  缓存持久化
                 */
                cacheManagerBuilder = cacheManagerBuilder.with(CacheManagerBuilder.persistence(new File(parameter.getCacheData(), topicName))).using(new DefaultDiskResourceService());
            }

            cacheManager = cacheManagerBuilder.build(true);

            if (null != parameter.getCaches() && parameter.getCaches().size() > 0) {
                /**
                 *  添加其他缓存
                 */
                parameter.getCaches().forEach(v -> {
                    cacheManager.createCache(v.getName(), CacheConfigurationBuilders.create(v.getKClass(), v.getVClass(), v.getHeap(), v.getHeapUnit(),
                        v.getOffheap(), v.getOffheapUnit(), v.getDisk(), v.getDiskUnit(), v.getDiskEnable(), (v.getTtl() == null || v.getTtl() == 0) ? null : Duration.ofSeconds(Double.valueOf(Double.valueOf(v.getTtl().toString()) * 0.8D).longValue())));

                    if(log.isDebugEnabled()){
                        log.debug("create ehcache3.0 name:{},key:{},value:{}",v.getName(),v.getKClass(),v.getVClass());
                    }
                });
            }
        }else{
            try {
                cacheManager = new EhcacheManager(new XmlConfiguration(new URL(parameter.getLoadingFile())));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                log.error("根据xml构建ehcache3.0 异常", e);
            }
        }





    }


    /**
     * Creates a {@link Cache} in this {@code CacheManager} according to the specified {@link CacheConfiguration}.
     * <p>
     * The returned {@code Cache} will be in status {@link Status#AVAILABLE AVAILABLE}.
     *
     * @param alias  the alias under which the cache will be created
     * @param config the configuration of the cache to create
     * @return the created and available {@code Cache}
     * @throws IllegalArgumentException if there is already a cache registered with the given alias
     * @throws IllegalStateException    if the cache creation fails
     */
    @Override
    public <K, V> Cache<K, V> createCache(String alias, CacheConfiguration<K, V> config) {
        return cacheManager.createCache(alias,config);
    }

    /**
     * Creates a {@link Cache} in this {@code CacheManager} according to the specified {@link CacheConfiguration} provided
     * through a {@link Builder}.
     * <p>
     * The returned {@code Cache} will be in status {@link Status#AVAILABLE AVAILABLE}.
     *
     * @param alias         the alias under which the cache will be created
     * @param configBuilder the builder for the configuration of the cache to create
     * @return the created and available {@code Cache}
     * @throws IllegalArgumentException if there is already a cache registered with the given alias
     * @throws IllegalStateException    if the cache creation fails
     */
    @Override
    public <K, V> Cache<K, V> createCache(String alias, Builder<? extends CacheConfiguration<K, V>> configBuilder) {
        return cacheManager.createCache(alias,configBuilder);
    }

    /**
     * Retrieves the {@link Cache} associated with the given alias, if one is known.
     *
     * @param alias     the alias under which to look the {@link Cache} up
     * @param keyType   the {@link Cache} key class
     * @param valueType the {@link Cache} value class
     * @return the {@link Cache} associated with the given alias, {@code null} if no such cache exists
     * @throws IllegalArgumentException if the keyType or valueType do not match the ones with which the
     *                                  {@code Cache} was created
     */
    @Override
    public <K, V> Cache<K, V> getCache(String alias, Class<K> keyType, Class<V> valueType) {
        return cacheManager.getCache(alias,keyType,valueType);
    }

    /**
     * Removes the {@link Cache} associated with the alias provided, if one is known.
     * <p>
     * When the cache is removed, it will release all resources it used.
     *
     * @param alias the alias for which to remove the {@link Cache}
     */
    @Override
    public void removeCache(String alias) {
        cacheManager.removeCache(alias);
    }

    /**
     * Transitions this {@code CacheManager} to {@link Status#AVAILABLE AVAILABLE}.
     * <p>
     * This will start all {@link Service Service}s managed by this {@code CacheManager}, as well
     * as initializing all {@link Cache}s registered with it.
     * <p>
     * If an error occurs before the {@code CacheManager} is {@code AVAILABLE}, it will revert to
     * {@link Status#UNINITIALIZED UNINITIALIZED} attempting to close all services it had already started.
     *
     * @throws IllegalStateException    if the {@code CacheManager} is not {@code UNINITIALIZED}
     * @throws StateTransitionException if the {@code CacheManager} could not be made {@code AVAILABLE}
     */
    @Override
    public void init() throws StateTransitionException {
        cacheManager.init();
    }

    /**
     * Transitions this {@code CacheManager} to {@link Status#UNINITIALIZED UNINITIALIZED}.
     * <p>
     * This will close all {@link Cache}s known to this {@code CacheManager} and stop all
     * {@link Service Service}s managed by this {@code CacheManager}.
     * <p>
     * Failure to close any {@code Cache} or to stop any {@code Service} will not prevent others from being closed or
     * stopped.
     *
     * @throws StateTransitionException if the {@code CacheManager} could not reach {@code UNINITIALIZED} cleanly
     * @throws IllegalStateException    if the {@code CacheManager} is not {@code AVAILABLE}
     */
    @Override
    public void close() throws StateTransitionException {
        cacheManager.close();
    }

    /**
     * Returns the current {@link Status Status} of this {@code CacheManager}.
     *
     * @return the current {@code Status}
     */
    @Override
    public Status getStatus() {
        return cacheManager.getStatus();
    }

    /**
     * Returns the current {@link Configuration} used by this {@code CacheManager}.
     *
     * @return the configuration instance backing this {@code CacheManager}
     */
    @Override
    public Configuration getRuntimeConfiguration() {
        return cacheManager.getRuntimeConfiguration();
    }
}
