package org.hehh.weChat;

import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:21
 * @description: 微信 access_token 存储
 */
public interface AccessTokenStorage {




    /**
     * 获得令牌
     *
     * @param appId 应用程序id
     * @return {@link Optional<String>}
     */
    Optional<String> getToken(String appId);

}
