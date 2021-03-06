package org.hehh.cloud.auth.token.impl.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.utils.bean.BeanKit;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.ParameterizedType;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * @author: HeHui
 * @create: 2020-03-15 20:48
 * @description: jwt生成token
 **/
@Slf4j
public class JwtGenerate<T extends LoginUser> {

    /**
     *  密钥
     */
    private final String SECRET;


    /**
     * 签名算法 ，将对token进行签名
     */
    private final SignatureAlgorithm signatureAlgorithm;

    private final Class<T> userClass;


    /**
     * 构建
     *
     * @param userClass 用户类
     * @param secret    秘密
     * @return {@link JwtGenerate<T>}
     */
    public static <T extends LoginUser> JwtGenerate<T> build(Class<T> userClass,String secret){
        return build(userClass,secret,SignatureAlgorithm.HS256);
   }

    /**
     * 构建
     *
     * @param userClass 用户类
     * @param secret    秘密
     * @param algorithm 算法
     * @return {@link JwtGenerate<T>}
     */
    public static <T extends LoginUser> JwtGenerate<T> build(Class<T> userClass,String secret,SignatureAlgorithm algorithm){
        return new JwtGenerate<>(secret,algorithm,userClass);
    }


    /**
     *  使用密钥构建jwt生成器
     *    默认使用 HS256 加密
     * @param secret 密钥
     */
    public JwtGenerate(String secret,Class<T> userClass){
        this(secret,SignatureAlgorithm.HS256,userClass);
    }



    /**
     *  使用密钥 和 指定加密方式构造器
     * @param secret 密钥
     * @param algorithm 加密方式
     */
    public JwtGenerate(String secret,SignatureAlgorithm algorithm,Class<T> userClass){
        this.SECRET = secret;
        this.signatureAlgorithm = algorithm;
        this.userClass = userClass;
    }




    /**
     * 生成Token
     * @param issuer
     *            该JWT的签发者，是否使用是可选的
     * @param user
     *            用户登陆信息
     * @return token String
     */
    public  String createJwtToken(String issuer, T user) {


        /** 生成签发时间 */
        long nowMillis = System.currentTimeMillis();
        if(user.getCreateTime() == null){
            user.setCreateTime(nowMillis);
        }


        /** 通过秘钥签名JWT */
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);

        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        /** Let's set the JWT Claims */
        JwtBuilder builder = Jwts.builder().setId(user.getToken())
                .setIssuedAt(new Date(nowMillis))
                .setClaims(BeanKit.toMap(user))
                .setSubject(user.getUserType()+"")
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        /** 如果指定的时间大于0 那么就设置过期时间 */
        if (user.getOverdueTime() >= 0) {
            long expMillis = nowMillis + user.getOverdueTime();
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }


        /**
         * 构建JWT并将其序列化为紧凑的、url安全的字符串
         */
        return builder.compact();

    }



    /**
     * 解析Claims
     *
     * @param token 生成的token
     * @return
     */
    public  Claims getClaim(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("解析Claims 错误:{}",e);
        }
        return claims;
    }




    /**
     * 解析Claims
     *
     * @param token 用户token
     * @return
     */
    public  Map<String,Object> getClaimMap(String token) {
        return getClaim(token);
    }

    /**
     * 解析Claims
     *
     * @param token 用户token
     * @return
     */
    public  T getUser(String token){
        try {
            return BeanKit.toBean(getClaimMap(token),userClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取jwt发布时间
     * @param token 用户token
     */
    public  Date getIssuedAt(String token) {
        Claims claim = getClaim(token);
        return null==claim?null:claim.getIssuedAt();
    }

    /**
     * 获取token-id
     * @param token 用户token
     */
    public  String getId(String token) {
        Claims claim = getClaim(token);
        return null==claim?null:claim.getId();
    }


    /**
     *  获取面向用户类型
     *
     * @param token 用户token
     * @return
     */
    public  String getSubject(String token){
        Claims claim = getClaim(token);
        return null == claim?null:claim.getSubject();
    }



    /**
     *  获取哟怒失效时间
     * @param token 令牌
     * @return 获取jwt失效时间
     */
    public  Date getExpiration(String token) {
        Claims claim = getClaim(token);
        return claim==null?null:claim.getExpiration();
    }


    /**
     * 验证token是否失效
     *
     * @param token
     * @return true:过期   false:没过期
     */
    public  boolean isExpired(String token) {
        try {
            Date expiration = getExpiration(token);
            if(null == expiration){
                return true;
            }
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }



}
