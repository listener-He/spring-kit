package org.hehh.wechat;

import org.hehh.weChat.AuthStorage;
import org.hehh.weChat.result.APITokenResult;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-08-09 15:23
 * @description: redis 微信授权存储
 */
public class RedisAuthStorage implements AuthStorage {

    private final RedisOperations<String,String> operations;

    public RedisAuthStorage(RedisOperations<String, String> operations) {
        this.operations = operations;
    }


    /**
     * 获得令牌
     *
     * @param appId 应用程序id
     * @return {@link Optional <String>}
     */
    @Override
    public Optional<String> getToken(String appId) {
        return Optional.ofNullable(operations.opsForValue().get(appId));
    }


    /**
     * 添加
     *
     * @param result 结果
     * @param appId  应用程序id
     */
    @Override
    public void put(APITokenResult result, String appId) {
        if(result != null && result.ok() && StringUtils.hasText(result.getAccess_token())){
            operations.opsForValue().set(appId,result.getAccess_token(),(result.getExpires_in() - 120), TimeUnit.SECONDS);
        }
    }
}
