package org.hehh.cloud.cache.ehcache3;

import lombok.Data;
import net.sf.ehcache.store.Policy;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.hehh.cloud.cache.CacheParameter;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author: HeHui
 * @date: 2020-07-31 01:10
 * @description: ehcache3.0 缓存参数
 */
@Data
public class EhCache3Parameter extends CacheParameter {

    /**
     * 设置缓存堆容纳元素个数(JVM内存空间)超出个数后会存到offheap中
     */
    private long heap = 100L;

    /**
     * 堆单元
     */
    private EntryUnit heapUnit = EntryUnit.ENTRIES;


    /**
     * 设置堆外储存大小(内存存储) 超出offheap的大小会淘汰规则被淘汰
     */
    private long offheap = 1048L;


    /**
     *  offheap的单位
     */
    @XmlAttribute
    private MemoryUnit offheapUnit = MemoryUnit.KB;


    /**
     * 配置磁盘持久化储存(硬盘存储)用来持久化到磁盘,这里设置为false不启用
     */
    private long disk = 1072L;


    /**
     * disk的单位
     */
    private MemoryUnit  diskUnit = MemoryUnit.KB;


    /**
     *  disk是否启用
     */
    private Boolean diskEnable = false;




    /**
     * 内存存储驱逐政策
     *   可选策略有：LRU（最近最少使用，默认策略）、FIFO（先进先出）、LFU（最少访问次数）。
     *   FIFO，先进先出。
     *   LFU， 缓存的元素有一个hit属性，hit值最小的将会被清出缓存。
     *   LRU，缓存的元素有一个时间戳，当缓存容量满了，而又需要腾出地方来缓存新的元素的时候，那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存。
     */
    private Policy memoryStoreEvictionPolicy;

}
