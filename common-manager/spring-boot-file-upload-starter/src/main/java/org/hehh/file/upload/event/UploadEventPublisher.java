package org.hehh.file.upload.event;


/**
 * @author: HeHui
 * @date: 2021-01-26 13:48
 * @description: 上传事件发布
 */
public interface UploadEventPublisher<T extends UploadEvent> {


    /**
     * 之前
     * 保存前
     *
     * @param event 事件
     */
    void before(T event);

    /**
     * 错误
     * 异常
     *
     * @param exception 异常
     * @param event     事件
     */
    void error(T event,Exception exception);

    /**
     * 后
     *
     * @param url   url
     * @param event 事件
     */
    void after(T event,String url);
}
