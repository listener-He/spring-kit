package org.hehh.cloud.cache;

import lombok.Data;
import net.sf.ehcache.store.Policy;
import net.sf.ehcache.store.disk.DiskStore;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-31 00:17
 * @description: ehcache2.0 缓存参数
 */
@Data
public class EhCache2Parameter extends CacheParameter {


    /**
     * 缓存最大数目
     */
    private int maxElementsInMemory;

    /**
     * 硬盘最大缓存个数。
     */
    private int maxElementsOnDisk;

    /**
     * 对象是否永久有效，一但设置了，timeout将不起作用。
     */
    private boolean eternal = false;

    /**
     * 是否保存到磁盘，当系统当机时
     */
    private boolean overflowToDisk = false;

    /**
     * 设置对象在失效前的允许闲置时间（单位：秒）。
     * 仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
     */
    private int timeToIdleSeconds = 1;


    /**
     *  设置对象在失效前允许存活时间（单位：秒）。
     *  最大时间介于创建时间和失效时间之间。
     *  仅当eternal=false对象不是永久有效时使用，
     *  默认是0.，也就是对象存活时间无穷大。
     *  private int timeToLiveSeconds = 0;
     */


    /**
     * 磁盘持久,是否缓存虚拟机重启期数据
     */
    private boolean diskPersistent = false;

    /**
     * 设置{@link DiskStore}（磁盘缓存）的缓存区大小。
     *  默认是30MB。每个Cache都应该有自己的一个缓冲区。
     */
    private int diskSpoolBufferSizeMB = 30;

    /**
     * 磁盘失效线程间隔秒 默认是120秒
     */
    private int diskExpiryThreadIntervalSeconds = 120;


    /**
     * 内存数量最大时是否清除。
     */
    private boolean clearOnFlush = true;


    /**
     * 内存存储驱逐政策
     *   可选策略有：LRU（最近最少使用，默认策略）、FIFO（先进先出）、LFU（最少访问次数）。
     *   FIFO，先进先出。
     *   LFU， 缓存的元素有一个hit属性，hit值最小的将会被清出缓存。
     *   LRU，缓存的元素有一个时间戳，当缓存容量满了，而又需要腾出地方来缓存新的元素的时候，那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存。
     */
    private Policy memoryStoreEvictionPolicy;

}
