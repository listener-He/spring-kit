package org.hehh.weChat;

import org.hehh.weChat.req.WxRequestParam;

import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:47
 * @description: 请求授权
 */
public interface RequestAuth {


    /**
     * 获得令牌
     *
     * @param param 参数
     * @return {@link Optional<String>}
     */
    Optional<String> getToken(WxRequestParam param);
}
