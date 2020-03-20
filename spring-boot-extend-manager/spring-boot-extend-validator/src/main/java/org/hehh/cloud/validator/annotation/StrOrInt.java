package org.hehh.cloud.validator.annotation;


import org.hehh.cloud.validator.IsStrOrIntValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * @author : HeHui
 * @date : 2019-04-21 01:41
 * @describe :
 */
@Target( {  METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsStrOrIntValidator.class)
@Documented
@Inherited
public @interface StrOrInt {

    /**
     *  异常消息
     * @return
     */
    String message() default "只支持数据或者字母";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
