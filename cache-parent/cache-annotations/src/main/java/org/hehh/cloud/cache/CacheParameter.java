package org.hehh.cloud.cache;

import lombok.Data;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-31 00:17
 * @description: 缓存参数
 */
@Data
public class CacheParameter implements java.io.Serializable {


    /**
     *  缓存名称
     */
    private String name;


    /**
     *  key 类型
     */
    private Class kClass;


    /**
     *  值类型
     */
    private Class vClass;


    /**
     *  缓存过期时间
     */
    private Long ttl;


    /**
     * 缓存过期时间单位
     */
    private TimeUnit ttlUnit;


    /**
     *  分组
     */
    private String grouping;


}
