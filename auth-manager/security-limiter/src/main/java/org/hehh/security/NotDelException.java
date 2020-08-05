package org.hehh.security;

/**
 * @author: HeHui
 * @date: 2020-06-22 09:54
 * @description: 不可删除移除
 */
public class NotDelException extends RuntimeException {




    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NotDelException(String message) {
        super(message);
    }
}
