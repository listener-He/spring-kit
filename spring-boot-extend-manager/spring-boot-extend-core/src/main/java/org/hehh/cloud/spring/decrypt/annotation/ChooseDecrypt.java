package org.hehh.cloud.spring.decrypt.annotation;

import java.lang.annotation.*;

/**
 * @author: HeHui
 * @date: 2020-04-12 02:48
 * @description: 选择解密器
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChooseDecrypt {

    /**
     *  解密器beanName
     * @return
     */
    String value();
}
