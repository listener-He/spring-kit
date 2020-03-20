package org.hehh.cloud.validator.annotation;


import org.hehh.cloud.validator.PropertyScriptAssertValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author: HeHui
 * @create: 2020-03-21 02:46
 * @description: 脚本验证
 **/
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PropertyScriptAssertValidator.class})
@Documented
public @interface PropertyScriptAssert {

    String message() default "{org.hibernate.validator.constraints.ScriptAssert.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String lang();

    /**
     *  脚本
     * @return
     */
    String script();

    /**
     *  别名
     * @return
     */
    String alias() default "_this";


    /**
     *  属性 出错后赋值属性
     * @return
     */
    String property();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        PropertyScriptAssert[] value();
    }
}
