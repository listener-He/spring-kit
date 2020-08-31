package org.hehh.cloud.security.oauth2.server.authorization.client;

/**
 * @author: HeHui
 * @date: 2020-08-29 22:46
 * @description: 注册客户端库
 */
public interface RegisteredClientRepository extends org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository {


    /**
     *  Delete the registered client identified by the provided {@code Id}
     *
     * @param id id
     */
    void deleteById(String id);


    /**
     *  Delete the registered client identified by the provided {@code clientId}.
     * @param clientId 客户机id
     */
    void deleteByClientId(String clientId);
}
