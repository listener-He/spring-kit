package org.hehh.weChat;

import org.hehh.weChat.constant.Oauth2API;
import org.hehh.weChat.result.APITokenResult;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:31
 * @description: 获取API access_token 请求
 */
public class APIAccessTokenRequest extends AbstractWxRequest<APITokenResult> {


    private final AuthStorage tokenStorage;


    /**
     * 文摘wx请求
     *
     * @param httpProxy http代理
     */
    public APIAccessTokenRequest(WxHttpProxy httpProxy,AuthStorage tokenStorage) {
        super(httpProxy);
        assert tokenStorage != null : "微信的API access_token 存储不能为空";
        this.tokenStorage = tokenStorage;
    }




    /**
     * 获取访问令牌
     *
     * @param appId     应用程序id
     * @param appSecret 应用程序的密钥
     * @return {@link String}
     */
    public String getAccessToken(String appId,String appSecret){
        assert appId != null : "APP-ID不能为空";
        assert appSecret != null : "APP-Secret不能为空";

        String key = appId + this.getClass().getName();

        return tokenStorage.getToken(key).orElseGet( ()->{
            APITokenResult result = getHttpProxy().get(String.format(Oauth2API.accessToken, appId, appSecret), APITokenResult.class);
            if(result != null){
                if(result.ok()){
                    tokenStorage.put(result,key);
                    return result.getAccess_token();
                }else{
                    //TODO if error ?
                }
            }
            return null;
        });
    }

}
