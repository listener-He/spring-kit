package org.hehh.utils.http.config;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-09 17:49
 * @description: http client 代理配置参数
 */
@Data
public class HttpClientProxyConfigurationParameters extends HttpProxyConfigurationParameters {


    /**
     *  默认路由的最大请求数
     */
    private int defaultMaxPerRoute = this.getMax();


    /**
     * 验证不活跃之后
     */
    private int validateAfterInactivity = 1000;


    /**
     * 从链接池获取连接的超时时间
     */
    private int connectionRequestTimeout = 2000;

    /**
     * 与服务器连接超时时间，创建socket连接的超时时间
     */
    private int connectTimeout = 2000;


    /**
     * socket读取数据的超时时间，从服务器获取数据的超时时间
     */
    private int socketTimeout = 10000;


    /**
     * 重试启用
     */
    private boolean retryEnabled = false;

    /**
     * 重试次数
     */
    private int retryCount = 3;
}
