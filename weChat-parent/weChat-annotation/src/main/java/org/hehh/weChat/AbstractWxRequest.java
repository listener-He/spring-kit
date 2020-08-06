package org.hehh.weChat;

import org.hehh.weChat.result.WxResult;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:34
 * @description: 抽象微信请求
 */
public abstract class AbstractWxRequest<T extends WxResult> implements WxRequest<T> {


    /**
     * http代理
     */
    private final WxHttpProxy httpProxy;


    /**
     * 文摘wx请求
     *
     * @param httpProxy http代理
     */
    protected AbstractWxRequest(WxHttpProxy httpProxy) {
        assert httpProxy != null : "http代理不能为空";
        this.httpProxy = httpProxy;
    }


    public WxHttpProxy getHttpProxy() {
        return httpProxy;
    }
}
