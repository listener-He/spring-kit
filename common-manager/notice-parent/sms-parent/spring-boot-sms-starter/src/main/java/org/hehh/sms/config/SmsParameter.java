package org.hehh.sms.config;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-17 00:05
 * @description: 短信参数
 */
@Data
public class SmsParameter {

    /**
     *  appID
     */
    private String accessKeyId;


    /**
     *  密钥
     */
    private String accessKeySecret;

    /**
     *  是否启用
     */
    private boolean enable = true;
}
