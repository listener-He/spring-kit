package org.hehh.security;

/**
 * @author: HeHui
 * @date: 2020-06-18 17:22
 * @description: ip规则定时刷新，清除过期数据
 */
public interface IpRuleRefresh {


    /**
     *  执行
     */
    void run();
}
