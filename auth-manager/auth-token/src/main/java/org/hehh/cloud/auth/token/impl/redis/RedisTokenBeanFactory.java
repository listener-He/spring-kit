package org.hehh.cloud.auth.token.impl.redis;

import org.hehh.cloud.auth.bean.login.LoginUser;
import org.hehh.cloud.auth.token.TokenManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 *
 * @author: HeHui
 * @date: 2020-08-06 00:49
 * @description: redis token Bean 工厂, 把一些统一的配置集中起来
 */
public interface RedisTokenBeanFactory {


    /**
     * 得到redis,连接工厂
     *
     * @return {@link RedisConnectionFactory}
     */
    RedisConnectionFactory getRedisConnectionFactory();


    /**
     * 得到redis,value序列化器
     *
     * @return {@link RedisSerializer<?>}
     */
    RedisSerializer<?> getValueRedisSerializer();


    /**
     * 获得关键的序列化器
     *
     * @return {@link RedisSerializer<?>}
     */
    RedisSerializer<?> getKeySerializer();






    /**
     * 构建
     *
     * @param userClass 用户类
     * @param secret    秘密
     * @return {@link TokenManager<T>}
     * @see RedisJwtTokenManager
     */
    <T extends LoginUser> TokenManager<T> build(Class<T> userClass,String secret);


    /**
     * 构建
     *@see RedisTokenManager
     * @return {@link TokenManager<T>}
     */
    <T extends LoginUser> TokenManager<T> build(Class<T> userClass);
}
