package org.hehh.cloud.auth.bean.login;

import lombok.Data;

/**
 * @author : HeHui
 * @date : 2019-03-07 12:18
 * @describe : 用户授权（权限列表）
 */
@Data
public class Auth implements java.io.Serializable {

    /**权限url*/
    private String url;

    /**请求方法*/
    private String requestMethod;

    /**权限名称*/
    private String authName;
}
