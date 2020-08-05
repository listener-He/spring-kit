package org.hehh.cloud.validator;

import org.hehh.cloud.validator.annotation.NotHasNull;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @author: HeHui
 * @create: 2020-03-21 02:35
 * @description: 验证集合中是否有null元素
 **/
public class NotHasNullValidator implements ConstraintValidator<NotHasNull, Collection<?>> {



    @Override
    public boolean isValid(Collection<?> objects, ConstraintValidatorContext constraintValidatorContext) {
        /**
         *  null不验证
         */
        if(null == objects){
            return true;
        }

        if(CollectionUtils.isEmpty(objects)){
            return false;
        }


        for (Object v : objects) {
            if(v == null){
                return false;
            }
        }


        return true;
    }
}
