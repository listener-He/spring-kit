package org.hehh.file.upload;


import org.hehh.file.upload.event.*;
import org.springframework.core.Ordered;

/**
 * @author: HeHui
 * @date: 2021-01-26 13:48
 * @description: 上传拦截器
 */
public interface UploadInterceptor<T extends UploadBase> extends Ordered {



    /**
     * 支持
     *
     * @param event 事件
     *
     * @return boolean
     */
    boolean supports(UploadEvent event);


    /**
     * 之前
     * 保存前
     *
     * @param event 事件
     */
    boolean before(UploadEvent event);

    /**
     * 错误
     * 异常
     *
     * @param exception 异常
     * @param event     事件
     */
    void error(UploadEvent event, Exception exception);

    /**
     * 后
     *
     * @param url   url
     * @param event 事件
     */
    void after(UploadEvent event, String url);


    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     *
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
