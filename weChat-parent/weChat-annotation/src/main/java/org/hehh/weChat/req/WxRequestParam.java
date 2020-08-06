package org.hehh.weChat.req;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:48
 * @description: 请求参数
 */
@Data
public class WxRequestParam implements java.io.Serializable {

    /**
     * 应用程序id
     */
    private String appId;
}
