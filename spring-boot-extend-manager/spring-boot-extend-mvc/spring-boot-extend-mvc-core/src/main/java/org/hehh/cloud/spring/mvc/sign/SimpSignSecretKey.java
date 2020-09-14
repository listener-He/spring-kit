package org.hehh.cloud.spring.mvc.sign;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-09-15 01:00
 * @description: 简单的签名密钥管理
 */
public class SimpSignSecretKey implements SignSecretKey {


    private final Map<String,String> secretMap = new HashMap<>();


    public SimpSignSecretKey(Map<String,String> secret){
        if(!CollectionUtils.isEmpty(secret)){
            this.secretMap.putAll(secret);
        }
    }

    /**
     * 得到的秘密
     *
     * @param appId 应用程序id
     * @return {@link String}
     */
    @Override
    public String getSecret(String appId) {
        return secretMap.get(appId);
    }
}
