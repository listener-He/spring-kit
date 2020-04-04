package org.hehh.cloud.spring.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author: HeHui
 * @date: 2020-04-04 15:24
 * @description: 参数解析器 客户端传递 body 参数
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {


    /**
     *   参数名称
     *      不填解析为参数名称
     *   特别注意：
     *       非简单类型参数下
     *           如果action参数只有一个那么此参数可以忽略
     *           如多个复杂类型 必须指定value值
     */
    String value()default "";

    /**
     *  是否必填
     */
    boolean required() default true;

    /**
     *   默认值
     */
    String defaultValue()default "";


    /**
     *  错误提示
     * @return
     */
    String error() default "";

}

