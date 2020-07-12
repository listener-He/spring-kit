package org.hehh.security.blocklist;


/**
 * @author: HeHui
 * @date: 2020-06-17 11:24
 * @description: 黑名单
 */
public interface BlockList {


    /**
     *  是否在黑名单中
     * @param ip 客户端地址
     * @return
     */
    boolean accept(String ip);

}
