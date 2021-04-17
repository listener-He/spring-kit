package org.hehh.weChat;

import org.hehh.weChat.result.APITokenResult;

import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:21
 * @description: 微信 授权 存储
 */
public interface AuthStorage {




    /**
     * 获得令牌
     *
     * @param appId 应用程序id
     * @return {@link Optional<String>}
     */
    Optional<String> getToken(String appId);


    /**
     *  添加
     *
     * @param result 结果
     * @param appId  应用程序id
     */
    void put(APITokenResult result,String appId);

}
