package org.hehh.weChat;

import org.hehh.utils.http.HttpRequestProxy;
import org.hehh.weChat.req.WxRequestParam;

import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-08-07 09:37
 * @description: 抽象的需要授权的请求
 */
public abstract class AbstractAuthAPIRequest extends AbstractWxRequest {


    private final RequestAuth auth;



    /**
     * 文摘wx请求
     *
     * @param httpProxy http代理
     * @param auth 接口授权
     */
    protected AbstractAuthAPIRequest(HttpRequestProxy httpProxy, RequestAuth auth) {
        super(httpProxy);
        assert auth != null : "接口授权不能位空";
        this.auth = auth;
    }



    /**
     * 身份验证
     *
     * @param param 参数
     * @return {@link Optional<String>}
     */
    protected Optional<String> auth(WxRequestParam param){
       return auth.getToken(param);
    }





}
