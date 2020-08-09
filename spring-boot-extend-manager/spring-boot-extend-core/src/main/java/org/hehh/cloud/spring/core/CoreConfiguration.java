package org.hehh.cloud.spring.core;

import cn.hutool.http.HttpUtil;
import org.hehh.cloud.spring.decrypt.DecryptManager;
import org.hehh.cloud.spring.userAgent.HuToolUserAgentProcessor;
import org.hehh.cloud.spring.userAgent.UserAgentConfiguration;
import org.hehh.cloud.spring.userAgent.UserAgentProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

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



    /**
     * hutool http配置工具
     *
     * @author hehui
     * @date 2020/08/10
     */
    @Configuration
    @ConditionalOnClass(HttpUtil.class)
    static class HuToolHttpConfiguration{


        @Bean
        @ConditionalOnMissingBean(UserAgentProcessor.class)
        public UserAgentProcessor userAgentProcessor(@Autowired(required = false) List<UserAgentConfiguration> configurations){
           return new HuToolUserAgentProcessor(configurations);
        }
    }

}
