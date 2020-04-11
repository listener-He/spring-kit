package org.hehh.cloud.validator;

import org.hehh.cloud.common.utils.StrKit;
import org.hehh.cloud.validator.annotation.StrOrInt;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : HeHui
 * @date : 2019-04-21 01:33
 * @describe : 是否数字or字母验证实现
 *
 */
public class IsStrOrIntValidator implements ConstraintValidator<StrOrInt,String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(null == s){
            return true;
        }
        return StrKit.isIntOrStr(s);
    }


    @Override
    public void initialize(StrOrInt constraintAnnotation) {

    }
}
