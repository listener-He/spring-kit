package org.hehh.cloud.auth.token.impl.redis;

import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.token.TokenManager;
import org.hehh.cloud.auth.token.TokenOutmodedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @create: 2020-03-15 22:05
 * @description: redis token管理器
 **/
@Slf4j
public class RedisTokenManager implements TokenManager {


    /**
     *  redis操作类型
     */
    private final RedisTemplate<String,LoginUser> redisService;



    /**
     *  带参数构造器
     * @param redisService
     */
    public RedisTokenManager(RedisTemplate<String,LoginUser> redisService){
        Assert.notNull(redisService,"初始化RedisTokenManager失败,redisService 不能为null!");
        this.redisService = redisService;
    }




    /**
     * 生成签名
     *
     * @param user 用户
     * @return 签名
     */
    @Override
    public String generateSign(LoginUser user) {
        Assert.notNull(user,"用户信息不能为空");
        Assert.hasText(user.getUserId(),"用户id不能为null");
        Assert.hasText(user.getToken(),"用户token不能为null");




        user.setCreateTime(System.currentTimeMillis());
        try {
            redisService.opsForValue().set(user.getToken(),user,user.getOverdueTime(), TimeUnit.MILLISECONDS);
        }catch (Exception e){
            log.error("生成登陆签名异常,原因:{}", e);
        }
        return user.getToken();
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
            return redisService.hasKey(token);
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
    public LoginUser getUser(String token) {
        if(StringUtils.hasText(token)){
            return redisService.opsForValue().get(token);
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
        return delay(getUser(token));
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

        return generateSign(user);
    }



    /**
     * 获取到期时间
     *
     * @param token 令牌
     * @return 秒
     */
    @Override
    public long getExpired(String token) throws TokenOutmodedException {
        if(StringUtils.hasText(token)){
            Long expire = redisService.getExpire(token, TimeUnit.MILLISECONDS);
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
            if(redisService.hasKey(token)){
                redisService.delete(token);
            }else{
                log.warn("token不存在,{}", token);
            }
        }
    }



}
