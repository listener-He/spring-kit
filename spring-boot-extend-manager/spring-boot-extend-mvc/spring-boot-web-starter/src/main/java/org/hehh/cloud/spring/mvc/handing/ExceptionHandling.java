package org.hehh.cloud.spring.mvc.handing;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.common.bean.result.Code;
import org.hehh.cloud.common.bean.result.ErrorResult;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.spring.exception.DecryptException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.LoginException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.sql.DataTruncation;
import java.util.List;
import java.util.Set;

/**
 * 统一异常处理
 * 扑捉 controller层未抓取的异常
 * 在方法上面加上
 * ExceptionHandler注解指定异常即可
 *
 * @author : HeHui
 * @date : 2019-02-26 11:16
 * @describe :
 */
@Slf4j
public class ExceptionHandling {


    /**
     * 数据库字段过长异常异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataTruncation.class)
    public Result dataTruncation(DataTruncation e) {
        String message = e.getMessage();
        String fieldName = "";
        if (message.endsWith("' at row 1")) {
            fieldName = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
        }
        return ErrorResult.error(Code.DATA, fieldName + "字段过长");
    }






    /**
     * 请求参数解密异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = DecryptException.class)
    public Result ParamDecryptionException(DecryptException e) {
        log.error("请求参数解密异常", e);
        return ErrorResult.error(Code.PARAM_DECRYPT_ERROR, "参数签名错误");
    }


    /**
     * 请求异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConnectException.class)
    public Result ConnectException(ConnectException e) {
        log.error("服务调度失败,异常", e);
        return new ErrorResult(Code.LINK_ERROR, "服务暂不可用");
    }


    /**
     * 登陆参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = LoginException.class)
    public Result ParamException(LoginException e) {
        log.error("登陆认证异常{}", e.getMessage());
        return ErrorResult.error(Code.PARAM_ERROR,"签名错误!");
    }






    /**
     * validated 注解验证异常
     *
     * @param e Validated异常
     * @return BackResult
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result javaVerifyException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        /**
         *   接收反馈的错误信息，取第一个就好了
         */
        StringBuilder errorParamName = new StringBuilder();
        if (CollUtil.isNotEmpty(constraintViolations)) {
            errorParamName.append(CollUtil.getFirst(constraintViolations).getMessage());
        }

        // 如果上面的循环没走，总要返回给用户一个什么信息吧
        if (errorParamName.length() == 0) {
            if (StrUtil.isBlank(e.getMessage())) {
                errorParamName.append("服务器繁忙.");
            } else {
                errorParamName.append(e.getMessage());
            }
        }

        log.error("参数认证异常{}", errorParamName);
        return new ErrorResult(Code.PARAM_ERROR, errorParamName.toString());
    }




    /**
     * validation异常捕获处理 （MethodArgumentNotValidException）
     *
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result serviceHandle(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return new ErrorResult(Code.PARAM_ERROR,  allErrors.get(0).getDefaultMessage());
    }
}
