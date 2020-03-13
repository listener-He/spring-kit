package com.hehh.cloud.common.bean.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : HeHui
 * @date : 2019-02-26 11:29
 * @describe : 错误返回类
 */
@Data
@NoArgsConstructor
public class ErrorResult<T> extends Result<T> {


    public ErrorResult(Code code, String msg, T t) {
        super(code.getCode(), msg, t, null);
        assert null != code : "code 不得等于null";

        if (code == Code.OK) {
            throw new RuntimeException("构建异常响应错误,异常时code不得等于0");
        }
        this.data = t;
    }


    public ErrorResult(Code code, String msg) {
        this(code, msg, null);
    }


    public static <T> ErrorResult<T> error(Code code, String msg, T data) {
        return new ErrorResult(code, msg, data);
    }

    public static <T> ErrorResult<T> error(String msg, T data) {
        return error(Code.SERVICE_ERROR, msg, data);
    }


    public static <T> ErrorResult<T> error(Code code, String msg) {
        return new ErrorResult(code, msg, null);
    }


    public static <T> ErrorResult<T> error(String msg) {
        return error(Code.FATAL_ERROR, msg);
    }


}
