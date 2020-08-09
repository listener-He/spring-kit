package org.hehh.utils.http.config;

import org.hehh.utils.http.browser.Browser;
import org.hehh.utils.http.browser.OS;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-10 00:04
 * @description: UserAgent 配置
 */
public interface UserAgentConfiguration {


    /**
     * 添加操作系统
     *
     * @param osList 操作系统列表
     */
    void addOS(List<OS> osList);


    /**
     * 添加浏览器
     *
     * @param browsers 浏览器
     */
    void addBrowser(List<Browser> browsers);
}
