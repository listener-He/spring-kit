package org.hehh.lock.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-07-27 22:27
 * @description: 锁注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface Lock {



    /**
     * Alias for {@link #prefix}.
     */
    @AliasFor("prefix")
    String value() default "";

    /**
     *  缓存前缀
     * @return
     */
    @AliasFor("value")
    String prefix() default "";



    /**
     *  分布式锁的key值，sqel表达式
     * @return
     */
    String key();


    /**
     *  锁的超时时间
     * @return
     */
    long timeout() default 5;


    /**
     *  超时时间的单位，默认为秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;


    /**
     *  执行完成后是否是否锁
     * @return
     */
    boolean releaseLock() default false;


    /**
     *  提示消息
     * @return
     */
    String message() default "";
}
