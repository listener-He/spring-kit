package org.hehh.cloud.spring.mvc.exception;

import org.hehh.security.limiter.LimiterException;

/**
 * @author: HeHui
 * @date: 2020-07-26 18:06
 * @description: 全局限流异常
 */
public class GlobalLimiterException extends RuntimeException {



    public GlobalLimiterException(LimiterException e){
        super(e.getMessage());
    }
}
