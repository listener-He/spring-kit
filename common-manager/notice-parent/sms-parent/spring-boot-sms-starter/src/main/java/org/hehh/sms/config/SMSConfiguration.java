package org.hehh.sms.config;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import org.hehh.aliyun.sms.AliyunSMSSend;
import org.hehh.sms.SMSSend;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: HeHui
 * @date: 2020-08-17 00:06
 * @description: 短信配置
 */
@Configuration
public class SMSConfiguration {


    /**
     * 阿里大鱼短信配置
     *
     * @author hehui
     * @date 2020/08/17
     */
    @Configuration
    @ConditionalOnClass(SendSmsRequest.class)
    @ConditionalOnMissingBean(SMSSend.class)
    static class AliyunSmsConfiguration{


        @Bean
        @ConfigurationProperties(prefix = "sms.aliyun")
        public SmsParameter smsParameter(){
            return new SmsParameter();
        }


        /**
         * 阿里大鱼短信发送
         *
         * @param parameter 参数
         * @return {@link SMSSend}
         */
        @Bean
        public SMSSend aliyunSMSSend(SmsParameter parameter){
            return new AliyunSMSSend(parameter.getAccessKeyId(),parameter.getAccessKeySecret(),parameter.isEnable());
        }

    }
}
