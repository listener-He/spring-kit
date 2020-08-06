package org.hehh.cloud.cache.memcached;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author: HeHui
 * @date: 2020-08-06 12:03
 * @description: memcached bean 工厂
 */
public class MemcachedClientBeanFactory implements InitializingBean {

    private final MemcacheCacheConfigurationParameter parameter;

    private MemcachedClient memcachedClient;

    public MemcachedClientBeanFactory (MemcacheCacheConfigurationParameter parameter) {
        this.parameter = parameter;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(parameter.getService()));


    }
}
