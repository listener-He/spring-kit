package org.hehh.cloud.spring.mvc.sign;

import java.lang.annotation.*;

/**
 * @author: HeHui
 * @date: 2020-09-14 22:34
 * @description: API签名注解标示
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Signature {




    /**
     *  需要忽略的表单参数
     * @return
     */
    String[] ignore() default {};


    /**
     *  过期时间
     * @return
     */
    long overdue() default -1;


}
