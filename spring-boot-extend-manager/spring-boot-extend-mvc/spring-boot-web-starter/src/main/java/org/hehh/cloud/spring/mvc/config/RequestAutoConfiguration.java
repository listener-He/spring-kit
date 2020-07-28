package org.hehh.cloud.spring.mvc.config;

import org.hehh.cloud.spring.core.CoreConfiguration;
import org.hehh.cloud.spring.decrypt.AesDecrypt;
import org.hehh.cloud.spring.decrypt.DesDecrypt;
import org.hehh.cloud.spring.decrypt.IDecrypt;
import org.hehh.cloud.spring.decrypt.RsaDecrypt;
import org.hehh.cloud.spring.decrypt.adapter.RequestBodyDecryptAdapter;
import org.hehh.cloud.spring.decrypt.param.DecryptParameter;
import org.hehh.cloud.spring.mvc.crypto.IDecryptAdapter;
import org.hehh.cloud.spring.mvc.filter.GlobalLimiterFilter;
import org.hehh.cloud.spring.mvc.parameter.GlobalLimitingParameter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author: HeHui
 * @create: 2020-03-22 00:39
 * @description: 配置类
 **/
@Configuration
@AutoConfigureAfter(CoreConfiguration.class)
public class RequestAutoConfiguration {


    /**
     *  全局限流配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.limiter.global")
    public GlobalLimitingParameter globalLimitingParameter(){
        return new GlobalLimitingParameter();
    }


    /**
     *  全局限流过滤器
     * @param parameter
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "spring.limiter.global.enable",havingValue = "true")
    public GlobalLimiterFilter globalLimiterFilter(GlobalLimitingParameter parameter){
        return new GlobalLimiterFilter(parameter);
    }


    /**
     *  参数配置
     * @return
     */
    @ConfigurationProperties("spring.decrypt")
    @Bean
    @ConditionalOnMissingBean(DecryptParameter.class)
    public DecryptParameter decryptParameter(){
        return new DecryptParameter();
    }


    /**
     *  aes解密
     * @param parameter
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "spring.decrypt.method",havingValue = "AES")
    public IDecrypt aesDecrypt(DecryptParameter parameter){
        return new AesDecrypt(parameter.getPrivateKey());
    }



    /**
     *  rsa解密
     * @param parameter
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnProperty(value = "spring.decrypt.method",havingValue = "RSA")
    public IDecrypt rsaDecrypt(DecryptParameter parameter){
        return new RsaDecrypt(parameter.getPrivateKey(),parameter.getPublicKey());
    }



    /**
     *  des解密
     * @param parameter
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "spring.decrypt.method",havingValue = "DEC")
    public IDecrypt desDecrypt(DecryptParameter parameter){
        return new DesDecrypt(parameter.getPrivateKey());
    }





}
