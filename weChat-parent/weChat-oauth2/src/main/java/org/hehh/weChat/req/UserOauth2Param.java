package org.hehh.weChat.req;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 10:20
 * @description: 用户授权参数
 */
@Data
public class UserOauth2Param extends WxRequestParam {

    /**
     * 应用密钥 AppSecret，在微信开放平台提交应用审核通过后获得
     */
    private String secret;

    /**
     * 填写第一步获取的 code 参数
     */
    private String code;
}
