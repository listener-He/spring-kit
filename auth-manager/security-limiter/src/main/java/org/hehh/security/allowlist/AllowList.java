package org.hehh.security.allowlist;

/**
 * @author: HeHui
 * @date: 2020-06-19 16:21
 * @description: 白名单
 */
public interface AllowList {


    /**
     *  是否在白名单中
     * @param ip 客户端地址
     * @return
     */
    boolean accept(String ip);
}
