package org.hehh.utils.http.userAgent;

/**
 * @author: HeHui
 * @date: 2020-08-10 00:57
 * @description: UserAgent 处理器
 */
public interface UserAgentProcessor {


    /**
     * 解析
     *
     * @param userAgentStr 用户代理str
     * @return {@link UserAgent}
     */
    UserAgent parse(String userAgentStr);
}
