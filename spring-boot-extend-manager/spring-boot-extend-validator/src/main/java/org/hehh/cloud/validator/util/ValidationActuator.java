package org.hehh.cloud.validator.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

/**
 * Validator 验证器 验证类
 *
 * @auther : HeHui
 * @date : 2020/7/26 12:49
 */
public class ValidationActuator {

    private Validator validator;

    public ValidationActuator(Validator validator) {
        assert validator != null : "验证器不能为空";
        this.validator = validator;
    }

    /**
     * 验证执行机构
     */
    public ValidationActuator() {
        this(Validation.buildDefaultValidatorFactory().getValidator());
    }


    /**
     * Gets validator.
     *
     * @return validator
     */
    @NonNull
    public Validator getValidator() {
        return validator;
    }


    /**
     * 验证bean返回原始结果
     *
     * @param obj    bean to be validated
     * @param groups validation group
     *
     * @throws ConstraintViolationException throw if validation failure
     */
    public Set<ConstraintViolation<Object>> validate(Object obj, Class<?>... groups) {

        Validator validator = getValidator();
        /**
         *  验证
         */
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj, groups);

        return constraintViolations;
    }


    /**
     * 执行一次验证bean
     *
     * @param obj    bean to be validated
     * @param groups validation group
     *
     * @throws ConstraintViolationException throw if validation failure
     */
    public void validateOf(Object obj, Class<?>... groups) {

        /**
         *  验证
         */
        Set<ConstraintViolation<Object>> constraintViolations = validate(obj, groups);
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            /**
             * 如果包含一些错误，则抛出约束违反异常
             */
            throw new ConstraintViolationException(constraintViolations);
        }
    }


    /**
     * 验证bean返回Map信息
     * key:value = field:message
     *
     * @param obj    bean to be validated
     * @param groups validation group
     *
     * @throws ConstraintViolationException throw if validation failure
     */
    public Map<String, String> tryValidate(Object obj, Class<?>... groups) {
        return mapWithValidError(validate(obj, groups));
    }


    /**
     * 将字段验证错误转换为标准的map型，key:value = field:message
     *
     * @param constraintViolations constraint violations(contain error information)
     *
     * @return error detail map
     */
    @NonNull
    public Map<String, String> mapWithValidError(Set<ConstraintViolation<Object>> constraintViolations) {
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return Collections.emptyMap();
        }

        Map<String, String> errMap = new HashMap<>(4);
        // Format the error message
        constraintViolations.forEach(
            constraintViolation ->
                errMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
        return errMap;
    }


    /**
     * 将字段验证错误转换为标准的map型，key:value = field:message
     *
     * @param fieldErrors 字段错误组
     *
     * @return 如果返回null，则表示未出现错误
     */
    public Map<String, String> mapWithFieldError(@Nullable List<FieldError> fieldErrors) {
        if (CollectionUtils.isEmpty(fieldErrors)) {
            return Collections.emptyMap();
        }
        Map<String, String> errMap = new HashMap<>(4);
        fieldErrors.forEach(filedError -> errMap.put(filedError.getField(), filedError.getDefaultMessage()));
        return errMap;
    }
}
