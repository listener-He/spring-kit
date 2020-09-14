package org.hehh.cloud.spring.mvc.sign;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-09-14 23:25
 * @description: 签名请求参数
 */
@Data
public class SignRequest {

    /**
     * 应用程序id
     */
    private String appId;

    /**
     * 应用的密钥
     */
    private String secret;

    /**
     * 标志
     */
    private String sign;

    /**
     * 时间戳
     */
    private long timestamp;
}
