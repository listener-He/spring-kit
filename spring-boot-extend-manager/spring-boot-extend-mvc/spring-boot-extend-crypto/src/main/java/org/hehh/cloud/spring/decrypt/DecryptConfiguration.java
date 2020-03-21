package org.hehh.cloud.spring.decrypt;

import org.hehh.cloud.spring.decrypt.param.DecryptParameter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author: HeHui
 * @create: 2020-03-22 00:39
 * @description: 解密配置类
 **/
@Configuration
public class DecryptConfiguration {


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
    @Primary
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
    @Primary
    @ConditionalOnProperty(value = "spring.decrypt.method",havingValue = "DEC")
    public IDecrypt desDecrypt(DecryptParameter parameter){
        return new DesDecrypt(parameter.getPrivateKey());
    }





}
