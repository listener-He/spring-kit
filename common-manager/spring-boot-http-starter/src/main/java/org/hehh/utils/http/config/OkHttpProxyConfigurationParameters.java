package org.hehh.utils.http.config;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-10 22:43
 * @description: okHttp代理参数配置
 */
@Data
public class OkHttpProxyConfigurationParameters extends HttpProxyConfigurationParameters {

    /**
     * 在连接重试失败
     */
    private boolean retryOnConnectionFailure = true;

    /**
     * 遵循重定向
     */
    private boolean followRedirects = true;

    /**
     * 遵循ssl重定向
     */
    private boolean followSslRedirects = true;


    /**
     * 验证不活跃之后
     */
    private int keepAliveDuration = 3000;

    /**
     * 是否开启缓存
     */
    private boolean cache = false;


    /**
     * 与服务器连接超时时间，创建socket连接的超时时间
     */
    private int connectTimeout = 3000;


    /**
     * socket读取数据的超时时间，从服务器获取数据的超时时间
     */
    private int readTimeout = 10000;


    /**
     *
     */
    private int writeTimeout = 10000;


    /**
     * 多久未读取超时
     */
    private int callTimeout = 0;

    /**
     * 只有http2和webSocket中使用 指定频率心跳
     */
    private int pingInterval = 0;


    private String ssl = "TLS";
}
