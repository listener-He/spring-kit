package org.hehh.utils.file.qiniu;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-22 18:03
 * @description: 七牛云配置参数
 */
@Data
public class QiniuyunConfigurationParameters {

    /**
     *  应用key
     */
    private String accessKey;

    /**
     *  密钥
     */
    private String secretKey;


    /**
     * 区
     */
    private  String zone = "";


    /**
     *  分片存储地址
     */
    private  String blockDirectory;

    /**
     * 默认的桶
     */
    private Bucket defaultBucket;
}
