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


    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public LimiterException(String message,int acquire, Throwable cause) {
        super(message, cause);
        this.acquire = acquire;
    }
}
