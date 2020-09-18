package org.hehh.weChat;

import org.hehh.utils.http.HttpRequestProxy;
import org.hehh.weChat.constant.Oauth2API;
import org.hehh.weChat.req.UserAuthCodeParam;
import org.hehh.weChat.req.UserDetailsParam;
import org.hehh.weChat.req.UserOauth2Param;
import org.hehh.weChat.result.UserAuthCodeResult;
import org.hehh.weChat.result.UserDetailsResult;
import org.hehh.weChat.result.UserOauth2Result;

import java.io.IOException;
import java.util.*;

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
    public UserInfoRequest(HttpRequestProxy httpProxy, RequestAuth auth) {
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

        try {
            return  getHttpProxy().get(String.format(Oauth2API.userInfo, optional.get(), param.getOpenId()),0).getData(UserDetailsResult.class);
        } catch (IOException e) {
        }
        return new UserDetailsResult("500","请求异常" );
    }




    /**
     *  用户授权
     *
     * @param param 参数
     * @return {@link UserOauth2Result}
     */
    public UserOauth2Result oauth2(UserOauth2Param param){
        try {
            return getHttpProxy().get(String.format(Oauth2API.oauth2, param.getAppId(), param.getSecret(),param.getCode()),0).getData(UserOauth2Result.class);
        } catch (IOException e) {
        }
        UserOauth2Result result = new UserOauth2Result();
        result.setErrcode("500");
        result.setErrmsg("请求异常");
        return result;
    }


    /**
     *  验证小程序登陆code授权
     *
     * @param param 参数
     * @return {@link UserAuthCodeResult}
     */
    public UserAuthCodeResult authCode(UserAuthCodeParam param){
        try {
            return getHttpProxy().get(String.format(Oauth2API.auth_code, param.getAppId(), param.getSecret(),param.getJs_code()),0).getData(UserAuthCodeResult.class);
        } catch (IOException e) {
        }
        UserAuthCodeResult result = new UserAuthCodeResult();
        result.setErrcode("500");
        result.setErrmsg("请求异常");
        return result;
    }


}
