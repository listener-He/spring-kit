package org.hehh.cloud.auth.token.core;

import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

/**
 * @author: HeHui
 * @create: 2020-03-15 23:57
 * @description: token参数配置
 **/
@ConfigurationProperties(prefix = "token")
@EnableConfigurationProperties
@Data
public class TokenParameter {


    /**
     *  使用那种管理器
     */
    private String manager = "jwt";


    /**
     *  密钥
     */
    private String secret;

    /**
     *
     *  发行人
     */
    private String issuer;



    /**
     *  redis配置
     */
    private RedisProperties redis;


}
