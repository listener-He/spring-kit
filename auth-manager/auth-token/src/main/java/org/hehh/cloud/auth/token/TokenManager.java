package org.hehh.cloud.auth.token;

import org.hehh.cloud.auth.bean.login.LoginUser;


/**
 * @author: HeHui
 * @create: 2020-03-15 20:39
 * @description: token管理生成
 **/
public interface TokenManager<T extends LoginUser> {




    /**
     *  生成签名
     * @param user 用户
     * @return 签名
     */
    String generateSign(T user);


    /**
     *  验证token是否有效
     * @param token 签名
     * @return 是否有效
     */
    boolean validity(String token);


    /**
     *  获取用户
     * @param token 签名
     * @return 签名用户
     */
    T getUser(String token);



    /**
     *  延长有效期
     * @param token 签名
     * @return 延长后的签名
     */
    String delay(String token) throws TokenOutmodedException;


    /**
     *  延长有效期
     * @param user 用户信息
     * @return
     * @throws TokenOutmodedException
     */
    String delay(T user) throws TokenOutmodedException;


    /**
     *  获取到期时间
     * @param token 令牌
     * @return 秒
     */
    long getExpired(String token) throws TokenOutmodedException;




    /**
     *  清除令牌
     * @param token 令牌
     */
    void remove(String token);


}
