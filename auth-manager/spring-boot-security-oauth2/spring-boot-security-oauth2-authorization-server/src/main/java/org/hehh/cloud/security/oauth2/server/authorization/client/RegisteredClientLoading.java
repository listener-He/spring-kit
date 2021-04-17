package org.hehh.cloud.security.oauth2.server.authorization.client;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-29 23:03
 * @description: 注册客户端加载
 */
public interface RegisteredClientLoading {



    /**
     * 加载
     *
     * @return {@link List<RegisteredClient>}
     */
    List<RegisteredClient> loading();
}
