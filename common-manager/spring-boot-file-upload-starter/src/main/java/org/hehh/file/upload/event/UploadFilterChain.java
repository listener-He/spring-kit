package org.hehh.file.upload.event;

import org.hehh.file.upload.UploadSupplier;

import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2021-01-29 15:22
 * @description: 上传过滤器链
 */
public interface UploadFilterChain {


    /**
     * 过滤器
     *
     * @param event 事件
     * @param other 其他
     *
     * @return {@link T}
     *
     * @throws IOException ioexception
     */
    <T> T doFilter(UploadEvent event, UploadSupplier<T> other) throws IOException;

    /**
     * 过滤器
     *
     * @param event 事件
     *
     * @return {@link T}
     *
     * @throws IOException ioexception
     */
    default <T> T doFilter(UploadEvent event) throws IOException {
        return this.doFilter(event, null);
    }


}
