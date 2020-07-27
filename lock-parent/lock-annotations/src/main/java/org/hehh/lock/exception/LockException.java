package org.hehh.lock.exception;

/**
 * @author: HeHui
 * @date: 2020-07-27 22:32
 * @description: 锁异常
 */
public class LockException extends RuntimeException {


    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public LockException(String message) {
        super(message);
    }
}
