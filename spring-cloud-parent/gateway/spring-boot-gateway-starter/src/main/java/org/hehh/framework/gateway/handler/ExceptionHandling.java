package org.hehh.framework.gateway.handler;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hehh.core.result.Code;
import org.hehh.core.result.ErrorResult;
import org.hehh.core.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.security.auth.login.LoginException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.sql.DataTruncation;
import java.util.List;
import java.util.Optional;
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
@RestControllerAdvice
public class ExceptionHandling {

    private final Log log = LogFactory.getLog(ExceptionHandling.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<Void>> error(Exception e) {
        log.error("[异常] system run error ", e);
        return Mono.just(Result.fault("系统异常"));
    }

    /**
     * 绑定错误
     *
     * @param e e
     * @return {@link Mono}<{@link Result}<{@link Void}>>
     */
    @ExceptionHandler(value = WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<Void>> bindError(WebExchangeBindException e) {
        log.error("[参数认证] request params bind verify", e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return Mono.just(new ErrorResult(Code.PARAM_ERROR, allErrors.get(0).getDefaultMessage()));
    }

    /**
     * 数据库字段过长异常异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataTruncation.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<Void>> dataTruncation(DataTruncation e) {
        String message = e.getMessage();
        String fieldName = "";
        if (message.endsWith("' at row 1")) {
            fieldName = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
        }
        return Mono.just(ErrorResult.error(Code.DATA, fieldName + "字段过长"));
    }


    /**
     * 请求异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConnectException.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<Void>> ConnectException(ConnectException e) {
        log.error("[系统] request service client connect error", e);
        return Mono.just(new ErrorResult(Code.LINK_ERROR, "服务暂不可用"));
    }


    /**
     * 登陆参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = LoginException.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<Void>> ParamException(LoginException e) {
        log.error("[登录认证] request user auth error ", e);
        return Mono.just(ErrorResult.error(Code.PARAM_ERROR, "签名错误!"));
    }


    /**
     * validated 注解验证异常
     *
     * @param e Validated异常
     * @return BackResult
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<Void>> javaVerifyException(ConstraintViolationException e) {
        log.error("[参数认证] service verify params", e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        String message = Optional.ofNullable(constraintViolations).filter(CollUtil::isNotEmpty).flatMap(l -> l.stream().findFirst()).map(ConstraintViolation::getMessage).orElse("服务器繁忙");
        return Mono.just(new ErrorResult(Code.PARAM_ERROR, message));
    }


    /**
     * validation异常捕获处理 （MethodArgumentNotValidException）
     *
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<Void>> serviceHandle(MethodArgumentNotValidException e) {
        log.error("[参数认证] request params verify", e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return Mono.just(new ErrorResult(Code.PARAM_ERROR, allErrors.get(0).getDefaultMessage()));
    }
}
