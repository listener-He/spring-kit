package org.hehh.cloud.validator.annotation;

import org.hehh.cloud.validator.NotHasNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author: HeHui
 * @create: 2020-03-21 02:32
 * @description: 验证集合中是否有空元素
 **/
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotHasNullValidator.class)
public @interface NotHasNull {

    String message() default "集合中不能含有null元素";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    /**
     * 定义List，为了让Bean的一个属性上可以添加多套规则
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        NotHasNull[] value();
    }
}
