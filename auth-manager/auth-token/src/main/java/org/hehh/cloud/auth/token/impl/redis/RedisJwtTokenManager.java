package org.hehh.cloud.auth.token.impl.redis;

import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.auth.bean.LoginUserConstant;
import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.token.TokenManager;
import org.hehh.cloud.auth.token.TokenOutmodedException;
import org.hehh.cloud.auth.token.impl.jwt.JwtGenerate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @create: 2020-03-15 22:25
 * @description: redis + token jwt管理
 **/
@Slf4j
public class RedisJwtTokenManager<T extends LoginUser> implements TokenManager<T> {



    /**
     *  该JWT的签发者，是否使用是可选的
     */
    private final String issuer;


    /**
     *  jwt生成
     */
    private final JwtGenerate<T> jwtGenerate;


    /**
     *  redis操作类型
     */
    private final RedisTemplate<String,Long> redisService;





    /**
     *   使用jwt密钥
     * @param secret jwt密钥
     */
    public RedisJwtTokenManager(String secret, RedisTemplate<String, Long> redisService,Class<T> userClass){
        this(secret,null,redisService,userClass);
    }


    /**
     *  使用jwt密钥并指定发行人
     * @param secret jwt密钥
     * @param issuer 发行人
     */
    public RedisJwtTokenManager(String secret,String issuer,RedisTemplate<String,Long> redisService,Class<T> userClass){
        Assert.hasText(secret,"jwt签名密钥secret不能为空");
        Assert.notNull(redisService,"初始化RedisJwtTokenManager失败,redisService 不能为null!");
        this.redisService = redisService;
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
        Assert.hasText(user.getToken(),"用户token不能为空");


        if(user.getCreateTime() == null){
            user.setCreateTime(System.currentTimeMillis());
        }

        /**
         *  jwt生成签名，jwt的有效时间 + 10年
         */
        user.setOverdueTime(user.getOverdueTime() + LoginUserConstant.JWT_INCREASE);

        String token = jwtGenerate.createJwtToken(issuer, user);


        /**
         *  存redis时就设置为传入的过期时间
         */
        redisService.opsForValue().set(user.getToken(),System.currentTimeMillis(),user.getOverdueTime() - LoginUserConstant.JWT_INCREASE, TimeUnit.MILLISECONDS);


        return token;

    }



    /**
     * 验证token是否有效
     *
     * @param token 签名
     * @return 是否有效
     */
    @Override
    public boolean validity(String token) {
        if(StringUtils.hasText(token)){
            LoginUser user = jwtGenerate.getUser(token);
            return null != user && redisService.hasKey(user.getToken());
        }
        return false;
    }


    /**
     * 获取用户
     *
     * @param token 签名
     * @return 签名用户
     */
    @Override
    public T getUser(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }

        T user = jwtGenerate.getUser(token);
        if(user != null){
            /**
             *  获取创建时间
             */
            Long createTime = redisService.opsForValue().get(user.getToken());
            if(createTime == null){
                return null;
            }

            return user;
        }

        return null;
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
     * @param user
     * @return
     * @throws TokenOutmodedException
     */
    @Override
    public String delay(LoginUser user) throws TokenOutmodedException {
        if(user == null){
            throw new TokenOutmodedException(null);
        }

        try {
            /**
             *  延长时间
             */
            redisService.opsForValue().set(user.getToken(),System.currentTimeMillis(),user.getOverdueTime() - LoginUserConstant.JWT_INCREASE, TimeUnit.MILLISECONDS);
            return user.getToken();
        }catch (Exception e){
            log.error("延长登陆时间异常,原因:{}", e);
        }

        return null;
    }





    /**
     * 获取到期时间
     *
     * @param token 令牌
     * @return 秒
     */
    @Override
    public long getExpired(String token) throws TokenOutmodedException {
        T user = jwtGenerate.getUser(token);

        if(user != null){
            Long expire = redisService.getExpire(user.getToken(), TimeUnit.MILLISECONDS);
            if(expire != null){
                return System.currentTimeMillis() + expire;
            }
        }

        return 0;
    }




    /**
     * 清除令牌
     *
     * @param token 令牌
     */
    @Override
    public void remove(String token) {
        if(StringUtils.hasText(token)){
            String id = jwtGenerate.getId(token);
            if(StringUtils.hasText(id) && redisService.hasKey(id)){
                redisService.delete(id);
            }else{
                log.warn("token不存在,{}", token);
            }
        }
    }



}
