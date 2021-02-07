package org.hehh.file.upload.event;

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
     *
     * @throws IOException ioexception
     */
    void doFilter(UploadEvent event) throws IOException;


}
