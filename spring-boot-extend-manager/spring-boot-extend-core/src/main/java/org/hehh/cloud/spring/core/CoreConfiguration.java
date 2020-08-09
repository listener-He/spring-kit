package org.hehh.cloud.spring.core;

import org.hehh.cloud.spring.decrypt.DecryptManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


/**
 * @author: HeHui
 * @create: 2020-03-21 03:13
 * @description: 核心配置类
 **/
@Configuration
public class CoreConfiguration {




    /**
     *  spring 上下文对象
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(SpringContextKit.class)
    public SpringContextKit springContextKit(){
        return new SpringContextKit();
    }


    /**
     *  解密管理器
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(DecryptManager.class)
    public DecryptManager decryptManager(SpringContextKit springContextKit){
        return new DecryptManager(springContextKit);
    }




}
