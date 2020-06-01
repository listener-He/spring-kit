package org.hehh.cloud.validator.annotation;

import org.hehh.cloud.validator.IsChineseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * @author : HeHui
 * @date : 2019-04-21 01:30
 * @describe : 是否中文验证注解
 */
@Target( { METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsChineseValidator.class)
@Inherited
@Documented
public @interface Chinese {


    /**
     *  异常消息
     * @return
     */
    String message() default "验证中文字符非法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    /**
     *  中文or非中文 验证
     * @return
     */
    boolean whether() default true;
}
