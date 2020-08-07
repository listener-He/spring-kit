package org.hehh.weChat.req;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 09:48
 * @description: 用户详情参数
 */
@Data
public class UserDetailsParam extends WxRequestParam {

    private String openId;
}
