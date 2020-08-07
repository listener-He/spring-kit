package org.hehh.weChat;

import lombok.Data;


/**
 * @author: HeHui
 * @date: 2020-08-06 19:53
 * @description: 配置参数
 */
@Data
public class WxConfigurationParameter {

    /**
     *  应用ID
     */
    private String appId;

    /**
     *  应用密钥
     */
    private String appSecret;
}
