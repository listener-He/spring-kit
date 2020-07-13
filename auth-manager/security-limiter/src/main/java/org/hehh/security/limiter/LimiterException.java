package org.hehh.security.limiter;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-07-13 15:24
 * @description: 限流异常
 */
@Data
public class LimiterException extends Exception {


    /**
     *  本次请求
     */
    private int acquire;

    /**
     *  剩余
     */
    private Double permits;


    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public LimiterException(String message,int acquire) {
        super(message);
        this.acquire = acquire;
    }
}
