package org.hehh.weChat;

import org.hehh.utils.http.HttpRequestProxy;
import org.hehh.weChat.constant.Oauth2API;
import org.hehh.weChat.result.APITokenResult;
import org.hehh.weChat.result.JSAPITicketResult;

import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:34
 * @description: 网页授权请求
 */
public class JSAPITicketRequest extends AbstractWxRequest {


    private final AuthStorage tokenStorage;


    /**
     * 文摘wx请求
     *
     * @param httpProxy http代理
     */
    public JSAPITicketRequest(HttpRequestProxy httpProxy, AuthStorage tokenStorage) {
        super(httpProxy);
        assert tokenStorage != null : "微信的API access_token 存储不能为空";
        this.tokenStorage = tokenStorage;
    }




    /**
     *  页面授权
     *
     * @param appId     应用程序id
     * @param access_token api 令牌
     * @return {@link String}
     */
    public String jsTicket(String appId,String access_token){
        assert appId != null : "APP-ID不能为空";
        assert access_token != null : "access_token不能为空";

        String key = appId + this.getClass().getName() + "#jsTicket";

        return tokenStorage.getToken(key).orElseGet( ()->{
            JSAPITicketResult result = null;
            try {
                result = getHttpProxy().get(String.format(Oauth2API.ticket, access_token,"jsapi")).getData(JSAPITicketResult.class);
            } catch (IOException e) {
                return null;
            }
            if(result != null){
                if(result.ok()){
                    APITokenResult tokenResult = new APITokenResult();
                    tokenResult.setAccess_token(result.getTicket());
                    tokenResult.setExpires_in(result.getExpires_in());
                    tokenStorage.put(tokenResult,key);
                    return result.getTicket();
                }else{
                    //TODO if error ?
                }
            }
            return null;
        });
    }


    /**
     *  app授权
     *
     * @param appId     应用程序id
     * @param access_token api 令牌
     * @return {@link String}
     */
    public String appTicket(String appId,String access_token){
        assert appId != null : "APP-ID不能为空";
        assert access_token != null : "access_token不能为空";

        String key = appId + this.getClass().getName() + "#appTicket";

        return tokenStorage.getToken(key).orElseGet( ()->{
            JSAPITicketResult result = null;
            try {
                result = getHttpProxy().get(String.format(Oauth2API.ticket, access_token,"app")).getData(JSAPITicketResult.class);
            } catch (IOException e) {
                return null;
            }
            if(result != null){
                if(result.ok()){
                    APITokenResult tokenResult = new APITokenResult();
                    tokenResult.setAccess_token(result.getTicket());
                    tokenResult.setExpires_in(result.getExpires_in());
                    tokenStorage.put(tokenResult,key);
                    return result.getTicket();
                }else{
                    //TODO if error ?
                }
            }
            return null;
        });
    }
}
