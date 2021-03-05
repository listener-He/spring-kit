package org.hehh.cloud.validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author: HeHui
 * @create: 2020-03-21 03:06
 * @description: 验证配置
 **/
@Configuration
@ConditionalOnProperty(name = "spring.validator.failFast", havingValue = "true")
public class ValidatorConfiguration {


    /**
     * 配置快速失败
     *
     * @return
     */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
