package org.hehh.security.allowlist;

import org.hehh.security.IpRule;
import org.hehh.security.NotDelException;

/**
 * @author: HeHui
 * @date: 2020-06-19 16:23
 * @description: 白名单操作类
 */
public interface AllowListOperation extends AllowList {


    /**
     *  添加一个白名单规则
     * @param rule 规则
     */
    void put(IpRule rule);


    /**
     *  移除一个白名单规则
     * @param rule 规则
     */
    void remove(IpRule rule) throws NotDelException;



    /**
     *  查询一个规则
     * @param ip
     * @return
     */
    boolean existing(String ip);


    /**
     * 是否在白名单中
     *
     * @param ip 客户端地址
     * @return
     */
    @Override
    default boolean accept(String ip){
        return this.existing(ip);
    }
}
