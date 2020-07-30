package org.hehh.cloud.cache.ehcache3;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

import java.time.Duration;

/**
 * @author: HeHui
 * @date: 2020-07-31 01:26
 * @description: ehcache3配置构造器
 */
public class CacheConfigurationBuilders {


    /**
     *  构建 CacheConfiguration 方法
     * @param kClass key的类型
     * @param vClass 值的类型
     * @param heap 设置缓存堆容纳元素个数(JVM内存空间)超出个数后会存到offheap中
     * @param heapUnit 堆单位
     * @param offheap 设置堆外储存大小(内存存储) 超出offheap的大小会淘汰规则被淘汰
     * @param offheapUnit 对外储存单位
     * @param disk 配置磁盘持久化储存(硬盘存储)用来持久化到磁盘
     * @param diskDisk 磁盘存储单位
     * @param diskEnable 磁盘存储是否启用
     * @param duration 过期时间配置
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> CacheConfiguration<K,V> create(Class<K> kClass, Class<V> vClass, long heap, EntryUnit heapUnit
        , long offheap, MemoryUnit offheapUnit, long disk, MemoryUnit diskDisk, boolean diskEnable, Duration duration){

        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder()
            .heap(heap, heapUnit)
            .offheap(offheap, offheapUnit);
        if(diskEnable){
            resourcePoolsBuilder = resourcePoolsBuilder.disk(disk, diskDisk,diskEnable);
        }
        CacheConfigurationBuilder<K, V> builder = CacheConfigurationBuilder.newCacheConfigurationBuilder(kClass, vClass, resourcePoolsBuilder.build());
        if(null != duration){
            builder = builder.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(duration));
        }
        return builder.build();
    }
}
