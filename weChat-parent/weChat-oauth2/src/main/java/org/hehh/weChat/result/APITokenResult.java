package org.hehh.weChat.result;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:24
 * @description: 获取API授权返回
 */
@Data
public class APITokenResult extends WxResult {


    /**
     *  接口授权
     */
    private String access_token;


    /**
     * 有效时间
     */
    private Integer expires_in;


    /**
     * 创建时间
     */
    private Long createTime;
}
