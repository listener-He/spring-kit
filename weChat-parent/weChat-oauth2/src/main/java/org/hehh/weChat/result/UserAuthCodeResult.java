package org.hehh.weChat.result;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 10:24
 * @description: 用户授权返回
 */
@Data
public class UserAuthCodeResult extends WxResult {



    /**
     * 用户授权密钥
     */
    private String session_key;

    /**
     * 授权用户唯一标识
     */
    private String openid;



    /**
     * 当且仅当该移动应用已获得该用户的 userinfo 授权时，才会出现该字段
     */
    private String unionid;


}
