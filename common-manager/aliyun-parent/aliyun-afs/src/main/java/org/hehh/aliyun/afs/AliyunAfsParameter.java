package org.hehh.aliyun.afs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: HeHui
 * @date: 2020-03-25 15:52
 * @description: 阿里云人机验证参数
 */
@Data
@ConfigurationProperties("aliyun.afs")
public class AliyunAfsParameter {


    /**
     *  区域ID
     */
    private String regionId;

    /**
     *  key
     */
    private String accessKey;

    /**
     *  密钥
     */
    private String accessKeySecret;

    /**
     *  产品key
     */
    private String appKey;


    /**
     *  验证模式
     */
    private String model;


    private boolean enable = false;
}
