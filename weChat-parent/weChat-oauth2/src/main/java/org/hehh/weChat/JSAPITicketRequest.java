package org.hehh.weChat;

import org.hehh.weChat.constant.Oauth2API;
import org.hehh.weChat.result.APITokenResult;
import org.hehh.weChat.result.JSAPITicketResult;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:34
 * @description: 网页授权请求
 */
public class JSAPITicketRequest extends AbstractWxRequest<JSAPITicketResult> {


    private final AuthStorage tokenStorage;


    /**
     * 文摘wx请求
     *
     * @param httpProxy http代理
     */
    public JSAPITicketRequest(WxHttpProxy httpProxy,AuthStorage tokenStorage) {
        super(httpProxy);
        assert tokenStorage != null : "微信的API access_token 存储不能为空";
        this.tokenStorage = tokenStorage;
    }




    /**
     * 获取访问令牌
     *
     * @param appId     应用程序id
     * @param access_token api 令牌
     * @return {@link String}
     */
    public String getTicket(String appId,String access_token){
        assert appId != null : "APP-ID不能为空";
        assert access_token != null : "access_token不能为空";

        String key = appId + this.getClass().getName();

        return tokenStorage.getToken(key).orElseGet( ()->{
            JSAPITicketResult result = getHttpProxy().get(String.format(Oauth2API.jSAPITicket, access_token), JSAPITicketResult.class);
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
