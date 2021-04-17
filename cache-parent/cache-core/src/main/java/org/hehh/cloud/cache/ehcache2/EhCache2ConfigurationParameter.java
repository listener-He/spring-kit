package org.hehh.cloud.cache.ehcache2;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.Configuration;
import org.hehh.cloud.cache.CacheConfigurationParameter;

/**
 * @author  HeHui
 * @date  2020-07-31 16:57
 * @description ehcache2.0 二级缓存配置
 */
public class EhCache2ConfigurationParameter extends CacheConfigurationParameter<EhCache2Parameter> {

    private String cacheName = Cache.DEFAULT_CACHE_NAME;

    /**
     * 本地最大字节堆
     */
    private Long localMaxBytesLocalHeap = Configuration.DEFAULT_MAX_BYTES_OFF_HEAP;

    /**
     * 本地最大字节本地磁盘
     */
    private Long localMaxBytesLocalDisk = Configuration.DEFAULT_MAX_BYTES_ON_DISK;


    public Long getLocalMaxBytesLocalHeap() {
        return localMaxBytesLocalHeap;
    }

    public void setLocalMaxBytesLocalHeap(Long localMaxBytesLocalHeap) {
        this.localMaxBytesLocalHeap = localMaxBytesLocalHeap;
    }

    public Long getLocalMaxBytesLocalDisk() {
        return localMaxBytesLocalDisk;
    }

    public void setLocalMaxBytesLocalDisk(Long localMaxBytesLocalDisk) {
        this.localMaxBytesLocalDisk = localMaxBytesLocalDisk;
    }


    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }
}
