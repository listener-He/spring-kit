package org.hehh.cloud.spring.decrypt.annotation;

import java.lang.annotation.*;

/**
 * @author: HeHui
 * @create: 2020-03-21 13:13
 * @description: 解密注解
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {

    /**
     *  解密的参数名称
     * @return
     */
    String value() default "";

}
