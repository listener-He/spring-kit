package org.hehh.weChat;

import org.hehh.weChat.result.APITokenResult;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:27
 * @description: map 存储 token
 */
public class SimpAuthStorage implements AuthStorage {


    private final Map<String,APITokenResult> storage = new ConcurrentHashMap<>(4);


    /**
     * 获得令牌
     *
     * @param appId 应用程序id
     * @return {@link Optional <String>}
     */
    @Override
    public Optional<String> getToken(String appId) {
        APITokenResult result = storage.get(appId);
        if(result != null && System.currentTimeMillis() < (result.getCreateTime() + ((result.getExpires_in() - 120) * 1000))){
            return Optional.ofNullable(result.getAccess_token());
        }
        if(result != null){
            storage.remove(appId);
        }

        return Optional.empty();
    }

    /**
     *  添加
     *
     * @param result 结果
     * @param appId  应用程序id
     */
    @Override
    public void put(APITokenResult result, String appId) {
        if(result.getCreateTime() == null){
            result.setCreateTime(System.currentTimeMillis());
        }
        storage.put(appId,result);
    }
}
