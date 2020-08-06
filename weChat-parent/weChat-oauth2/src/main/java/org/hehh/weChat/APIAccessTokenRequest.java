package org.hehh.weChat;

import org.hehh.weChat.constant.Oauth2API;
import org.hehh.weChat.result.APITokenResult;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:31
 * @description: 获取API access_token 请求
 */
public class APIAccessTokenRequest extends AbstractWxRequest<APITokenResult> {


    private AccessTokenStorage tokenStorage;


    /**
     * 文摘wx请求
     *
     * @param httpProxy http代理
     */
    protected APIAccessTokenRequest(WxHttpProxy httpProxy,AccessTokenStorage tokenStorage) {
        super(httpProxy);
        assert tokenStorage != null : "微信的API access_token 存储不能为空";
        this.tokenStorage = tokenStorage;
    }



    public String getAccessToken(String appId,String appSecret){
        assert appId != null : "APP-ID不能为空";
        assert appSecret != null : "APP-Secret不能为空";

        String s = tokenStorage.getToken(appId).orElseGet( ()->{


            APITokenResult result = getHttpProxy().get(String.format(Oauth2API.accessToken, appId, appSecret), APITokenResult.class);

            return "null";
        });

        return s;
    }

}
