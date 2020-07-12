package org.hehh.cloud.auth.bean;


import org.hehh.cloud.auth.bean.login.LoginUser;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author: HeHui
 * @create: 2019-10-14 18:59
 * @description: 登陆用户解析器
 **/
public class LoginUserConstant {


    /**
     *  token名
     */
    public static final String USER_TOKEN_HEADER = "access_token";



    public static final String USER_TOKEN_ATTRIBUTE = "user_token_attribute";


    /**
     *  LoginUser 的过期时间是redis的过期时间
     *    为了延长用户登陆时间时token无变化
     */
    public static final long JWT_INCREASE = 315364000000L;

}
