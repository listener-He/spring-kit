package org.hehh.cloud.spring.mvc.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author: HeHui
 * @date: 2020-04-13 00:34
 * @description: 必填声明
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Required {



    @AliasFor("massage")
    String value() default "";

    /**
     *  提示消息
     * @return
     */
    @AliasFor("value")
    String massage() default "";
}
