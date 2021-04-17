package org.hehh.cloud.spring.mvc.sign;

/**
 * @author: HeHui
 * @date: 2020-09-15 00:19
 * @description: 签名密钥
 */
public interface SignSecretKey {

    /**
     * 得到的秘密
     *
     * @param appId 应用程序id
     * @return {@link String}
     */
    String getSecret(String appId);
}
