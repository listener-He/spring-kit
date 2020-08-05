package org.hehh.cloud.cache.ehcache2;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import org.hehh.cloud.cache.CacheConfigurationParameter;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @author: HeHui
 * @date: 2020-07-31 16:27
 * @description: ehcache2.0 构建器
 */
public class EhCache2Builders {


    /**
     * 构建器
     *
     * @param parameter 参数
     * @return {@link CacheManager}
     */
    public static CacheManager builder(CacheConfigurationParameter<EhCache2Parameter> parameter){

        if(StringUtils.hasText(parameter.getLoadingFile())){
            return new CacheManager(parameter.getLoadingFile());
        }

        Configuration configuration = new Configuration();
        configuration.setName(Cache.DEFAULT_CACHE_NAME);
//        configuration.setMaxBytesLocalHeap(localMaxBytesLocalHeap);
//        configuration.setMaxBytesLocalDisk(localMaxBytesLocalDisk);

        /**
         * 每次启动设置新的文件地址,以避免重启期间一级缓存未同步,以及单机多应用启动造成EhcacheManager重复的问题.
         */
        DiskStoreConfiguration dsc = new DiskStoreConfiguration();
        dsc.setPath(parameter.getCacheData());
        configuration.diskStore(dsc);

        if(parameter.getCaches() != null && parameter.getCaches().size() > 0){
                for (EhCache2Parameter cache2Parameter : parameter.getCaches()) {
                    /**
                     * cache
                     */
                    CacheConfiguration cacheConfiguration = new CacheConfiguration();
                    cacheConfiguration.setEternal(false);
                    cacheConfiguration.memoryStoreEvictionPolicy(cache2Parameter.getMemoryStoreEvictionPolicy());
                    cacheConfiguration.setDiskExpiryThreadIntervalSeconds(cache2Parameter.getDiskExpiryThreadIntervalSeconds());
                    // 默认false,使用引用.设置为true,避免外部代码修改了缓存对象.造成EhCache的缓存对象也随之改变
                    // 但是设置为true后,将引起element的tti不自动刷新.如果直接新建element去覆盖原值.则本地ttl和远程ttl会产生一定的误差.
                    // 因此,使用时放弃手动覆盖方式刷新本地tti,当本地tti过期后,自动从Redis中再获取即可.
                    cacheConfiguration.copyOnRead(true);
                    cacheConfiguration.copyOnWrite(true);
                    cacheConfiguration.setTimeToIdleSeconds(cache2Parameter.getTimeToIdleSeconds());

                    if(cache2Parameter.getTtl() != null){
                        Duration.ofSeconds(cache2Parameter.getTtlUnit().toSeconds(cache2Parameter.getTtl()));
                        cacheConfiguration.setTimeToLiveSeconds(cache2Parameter.getTtl());
                    }

                    cacheConfiguration.setName(cache2Parameter.getName());
                    configuration.addCache(cacheConfiguration);
                }
            }
            //configuration.setDefaultCacheConfiguration(null);
            configuration.setDynamicConfig(false);
        
           return new CacheManager(configuration);
        }




}
