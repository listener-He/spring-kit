package org.hehh.cloud.cache;

import lombok.Data;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-07-31 01:23
 * @description: 缓存配置参数
 */
@Data
public class CacheConfigurationParameter<T extends CacheParameter> {

    /**
     *  缓存目录
     */
    private String cacheData;


    /**
     *  加载文件（一级缓存用）
     */
    private String loadingFile;


    /**
     *  缓存
     */
    private List<T> caches;


    /**
     *  缓存同步 topic
     */
    private String topicName;

}
