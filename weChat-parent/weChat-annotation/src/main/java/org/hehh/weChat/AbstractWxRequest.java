package org.hehh.weChat;


import org.hehh.utils.http.HttpRequestProxy;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:34
 * @description: 抽象微信请求
 */
public abstract class AbstractWxRequest implements WxRequest {


    /**
     * http代理
     */
    private final HttpRequestProxy httpProxy;


    /**
     * 文摘wx请求
     *
     * @param httpProxy http代理
     */
    protected AbstractWxRequest(HttpRequestProxy httpProxy) {
        assert httpProxy != null : "http代理不能为空";
        this.httpProxy = httpProxy;
    }


    protected HttpRequestProxy getHttpProxy() {
        return httpProxy;
    }
}
