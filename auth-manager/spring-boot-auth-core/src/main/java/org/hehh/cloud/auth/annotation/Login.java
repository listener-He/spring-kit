package org.hehh.cloud.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : HeHui
 * @date : 2019-03-05 18:09
 * @describe : 登陆
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {

    /**
     *  是否必须登陆
     * @return
     */
    boolean isMust() default true;

    /**
     *  用户类型
     * @return
     */
    int[] userType() default {};


}
