package org.hehh.cloud.auth.token.impl.jwt;

import cn.hutool.core.util.IdUtil;
import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.token.TokenManager;
import org.hehh.cloud.auth.token.TokenOutmodedException;

import java.util.Date;

/**
 * @author: HeHui
 * @create: 2020-03-15 20:43
 * @description: jwt生成token
 **/
public class JwtTokenManager implements TokenManager {


    /**
     *  该JWT的签发者，是否使用是可选的
     */
    private final String issuer;


    /**
     *  jwt生成
     */
    private final JwtGenerate jwtGenerate;


    /**
     *   使用jwt密钥
     * @param secret jwt密钥
     */
    public JwtTokenManager(String secret){
       this(secret,null);
    }


    /**
     *  使用jwt密钥并指定发行人
     * @param secret jwt密钥
     * @param issuer 发行人
     */
    public JwtTokenManager(String secret,String issuer){
        this.jwtGenerate = new JwtGenerate(secret);
        this.issuer = issuer;
    }








    /**
     * 生成签名
     *
     * @param user 用户
     * @return 签名
     */
    @Override
    public String generateSign(LoginUser user) {
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
    public LoginUser getUser(String token) {
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
        LoginUser user = jwtGenerate.getUser(token);
        if(user == null){
            throw new TokenOutmodedException(token);
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





    public static void main(String[] args) {
        JwtTokenManager j = new JwtTokenManager("123456");

        LoginUser user = new LoginUser();
        user.setUserId("1");
        user.setAppId(1);
        user.setName("测试");
        user.setToken(IdUtil.fastSimpleUUID());
        user.setAssociatedId("1");
        user.setUserType(1);
        user.setEquipment(1);
        user.setOverdueTime(7200000);

        String s = j.generateSign(user);


        System.out.println(s);

        LoginUser user1 = j.getUser(s);


        String delay = j.delay(s);


        System.out.println(delay);

    }
}
