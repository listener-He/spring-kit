package org.hehh.utils.http.userAgent;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-10 00:55
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


    /**
     * 添加平台
     *
     * @param platforms 平台
     */
    void addPlatform(List<Platform> platforms);
}
