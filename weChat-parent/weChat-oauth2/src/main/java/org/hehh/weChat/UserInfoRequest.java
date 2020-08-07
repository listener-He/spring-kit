package org.hehh.weChat;

import org.hehh.weChat.constant.Oauth2API;
import org.hehh.weChat.req.UserDetailsParam;
import org.hehh.weChat.req.UserOauth2Param;
import org.hehh.weChat.result.UserDetailsResult;
import org.hehh.weChat.result.UserOauth2Result;

import java.util.Optional;

/**
 * @author: HeHui
 * @date: 2020-08-07 09:35
 * @description: 获取用户信息请求
 */
public class UserInfoRequest extends AbstractAuthAPIRequest {


    /**
     * 文摘wx请求
     *
     * @param httpProxy http代理
     * @param auth      接口授权
     */
    protected UserInfoRequest(WxHttpProxy httpProxy, RequestAuth auth) {
        super(httpProxy, auth);
    }




    /**
     *  获取用户详情
     *
     * @param param 参数
     * @return {@link UserDetailsResult}
     */
    public UserDetailsResult details(UserDetailsParam param){
        Optional<String> optional = super.auth(param);
        if(!optional.isPresent()){
            return new UserDetailsResult("401","API无法授权" );
        }
        
      return  getHttpProxy().get(String.format(Oauth2API.userInfo, optional.get(), param.getOpenId()),UserDetailsResult.class,0);
    }




    /**
     *  用户授权
     *
     * @param param 参数
     * @return {@link UserOauth2Result}
     */
    public UserOauth2Result oauth2(UserOauth2Param param){
        return  getHttpProxy().get(String.format(Oauth2API.oauth2, param.getAppId(), param.getSecret(),param.getCode()),UserOauth2Result.class,0);
    }


}
