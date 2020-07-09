package org.hehh.cloud.validator;

import org.hehh.cloud.validator.annotation.Chinese;
import org.hehh.utils.StrKit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : HeHui
 * @date : 2019-04-21 01:33
 * @describe : 是否中文验证实现
 */
public class IsChineseValidator implements ConstraintValidator<Chinese,String> {

    private boolean whether;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(null == s){
            return true;
        }
        boolean containChinese = StrKit.isContainChinese(s);
        if(whether){
            return containChinese;
        }
        return !containChinese;
    }


    @Override
    public void initialize(Chinese constraintAnnotation) {
        this.whether = constraintAnnotation.whether();
    }
}
