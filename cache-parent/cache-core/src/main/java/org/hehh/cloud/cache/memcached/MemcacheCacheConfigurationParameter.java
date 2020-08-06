package org.hehh.cloud.cache.memcached;

import org.hehh.cloud.cache.CacheConfigurationParameter;
import org.hehh.cloud.cache.CacheParameter;

/**
 * @author: HeHui
 * @date: 2020-08-06 11:32
 * @description: memcached 缓存配置
 */
public class MemcacheCacheConfigurationParameter extends CacheConfigurationParameter<CacheParameter> {


    public String service;


    public String getService() {
        return service;
    }
}
