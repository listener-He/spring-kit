package org.hehh.mail.config;

import org.hehh.mail.JavaMailSenderProxyImpl;
import org.hehh.mail.MailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author: HeHui
 * @date: 2020-08-16 23:22
 * @description: 邮件发送配置
 */
@Configuration
public class MailConfiguration {


    /**
     *  配置bean
     * @param mailSender
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(MailSender.class)
    @ConditionalOnBean(JavaMailSenderImpl.class)
    public MailSender javaMailSenderProxyImpl(JavaMailSenderImpl mailSender){
        return new JavaMailSenderProxyImpl(mailSender);
    }
}
