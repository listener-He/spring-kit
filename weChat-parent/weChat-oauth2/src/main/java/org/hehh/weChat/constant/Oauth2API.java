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
        "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}";


    /**页面授权api*/
    public static final String jSAPITicket =
        "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={}&type=jsapi";


    /**
     *  授权
     */
    public static final String oauth2 =
        "https://api.weixin.qq.com/sns/oauth2/access_token?appid={}&secret={}&code={}&grant_type=authorization_code";


    /**
     *  用户详情
     */
    public static final String userInfo =
        "https://api.weixin.qq.com/cgi-bin/user/info?access_token={}&openid={}&lang=zh_CN";
}
