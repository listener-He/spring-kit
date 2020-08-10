package org.hehh.weChat.constant;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:19
 * @description: 授权API
 */
public class Oauth2API {

    /**
     * 获取token api
     */
    public static final String accessToken =
        "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";


    /**应用授权api*/
    public static final String ticket =
        "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=%s";


    /**
     * 小程序 登录凭证校验
     *  https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     */
    public static final String auth_code = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    /**
     *  授权
     */
    public static final String oauth2 =
        "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";


    /**
     *  用户详情
     */
    public static final String userInfo =
        "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
}
