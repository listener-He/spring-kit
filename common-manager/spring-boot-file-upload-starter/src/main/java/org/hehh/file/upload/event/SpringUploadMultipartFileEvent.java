package org.hehh.file.upload.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author: HeHui
 * @date: 2021-01-27 17:31
 * @description: spring 事件
 */
public class SpringUploadMultipartFileEvent extends ApplicationEvent {

    private final UploadMultipartFileEvent event;

    private Exception exception;

    private String url;

    /**
     * 状态 1前，2异常，3成功
     */
    private final int state;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param event
     * @param state
     */
    public SpringUploadMultipartFileEvent(Object source, UploadMultipartFileEvent event, int state) {
        super(source);
        this.event = event;
        this.state = state;
    }


    public UploadMultipartFileEvent getEvent() {
        return event;
    }


    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }


    /**
     * 之前
     *
     * @param source 源
     * @param event  事件
     *
     * @return {@link SpringUploadMultipartFileEvent}
     */
    public static SpringUploadMultipartFileEvent before(Object source, UploadMultipartFileEvent event) {
        return new SpringUploadMultipartFileEvent(source, event, 0);
    }


    /**
     * 错误
     *
     * @param source 源
     * @param event  事件
     * @param e      e
     *
     * @return {@link SpringUploadMultipartFileEvent}
     */
    public static SpringUploadMultipartFileEvent error(Object source, UploadMultipartFileEvent event,Exception e) {
        SpringUploadMultipartFileEvent multipartFileEvent = new SpringUploadMultipartFileEvent(source, event, 1);
        multipartFileEvent.setException(e);
        return multipartFileEvent;
    }

    /**
     * 后
     *
     * @param source 源
     * @param event  事件
     * @param url    url
     *
     * @return {@link SpringUploadMultipartFileEvent}
     */
    public static SpringUploadMultipartFileEvent after(Object source, UploadMultipartFileEvent event,String url) {
        SpringUploadMultipartFileEvent multipartFileEvent = new SpringUploadMultipartFileEvent(source, event, 2);
        multipartFileEvent.setUrl(url);
        return multipartFileEvent;
    }
}
