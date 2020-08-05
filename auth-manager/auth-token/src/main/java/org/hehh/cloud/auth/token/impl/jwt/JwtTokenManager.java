package org.hehh.cloud.auth.token.impl.jwt;

import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.token.TokenManager;
import org.hehh.cloud.auth.token.TokenOutmodedException;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.Date;

/**
 * @author: HeHui
 * @create: 2020-03-15 20:43
 * @description: jwt生成token
 **/
@Slf4j
public class JwtTokenManager<T extends LoginUser> implements TokenManager<T> {


    /**
     *  该JWT的签发者，是否使用是可选的
     */
    private final String issuer;


    /**
     *  jwt生成
     */
    private final JwtGenerate<T> jwtGenerate;


    /**
     *   使用jwt密钥
     * @param secret jwt密钥
     */
    public JwtTokenManager(String secret,Class<T> userClass){
       this(secret,null,userClass);
    }


    /**
     *  使用jwt密钥并指定发行人
     * @param secret jwt密钥
     * @param issuer 发行人
     */
    public JwtTokenManager(String secret,String issuer,Class<T> userClass){
        Assert.hasText(secret,"jwt签名密钥secret不能为空");
        this.jwtGenerate =  JwtGenerate.build(userClass,secret);
        this.issuer = issuer;
    }








    /**
     * 生成签名
     *
     * @param user 用户
     * @return 签名
     */
    @Override
    public String generateSign(T user) {

        Assert.notNull(user,"用户信息不能为空");
        Assert.hasText(user.getUserId(),"用户id不能为空");
        Assert.hasText(user.getToken(),"登陆token不能为空");

        return jwtGenerate.createJwtToken(issuer,user);
    }





    /**
     * 验证token是否有效
     *
     * @param token 签名
     * @return 是否有效
     */
    @Override
    public boolean validity(String token) {
        return !jwtGenerate.isExpired(token);
    }



    /**
     * 获取用户
     *
     * @param token 签名
     * @return 签名用户
     */
    @Override
    public T getUser(String token) {
        return jwtGenerate.getUser(token);
    }




    /**
     * 延长有效期
     *
     * @param token 签名
     * @return 延长后的签名
     */
    @Override
    public String delay(String token) throws TokenOutmodedException {

        return delay(jwtGenerate.getUser(token));
    }


    /**
     * 延长有效期
     *
     * @param user 用户信息
     * @return
     * @throws TokenOutmodedException
     */
    @Override
    public String delay(T user) throws TokenOutmodedException {
        if(user == null){
            throw new TokenOutmodedException(null);
        }

        return jwtGenerate.createJwtToken(issuer,user);
    }

    /**
     * 获取到期时间
     *
     * @param token 令牌
     * @return 秒
     */
    @Override
    public long getExpired(String token) throws TokenOutmodedException {
        Date expiration = jwtGenerate.getExpiration(token);
        if(expiration == null){
            throw new TokenOutmodedException(token);
        }
        return expiration.getTime();
    }


    /**
     * 清除令牌
     *
     * @param token 令牌
     */
    @Override
    public void remove(String token) {
        log.warn("jwt无法清除token");
    }




}
