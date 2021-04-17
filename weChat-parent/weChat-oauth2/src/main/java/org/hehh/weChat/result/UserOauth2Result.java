package org.hehh.weChat.result;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 10:24
 * @description: 用户授权返回
 */
@Data
public class UserOauth2Result extends WxResult {

    /**
     * 接口调用凭证
     */
    private String access_token;

    /**
     * access_token 接口调用凭证超时时间，单位（秒）
     */
    private Integer expires_in;

    /**
     * 用户刷新 access_token
     */
    private String refresh_token;

    /**
     * 授权用户唯一标识
     */
    private String openid;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;

    /**
     * 当且仅当该移动应用已获得该用户的 userinfo 授权时，才会出现该字段
     */
    private String unionid;


}
