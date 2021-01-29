package org.hehh.file.upload.event;

import org.hehh.file.upload.req.UploadBase;

/**
 * @author: HeHui
 * @date: 2021-01-28 13:44
 * @description: 上传事件
 */
public class UploadEvent {

    /**
     * 上传
     */
    private Object event;

    /**
     * 是否完成
     */
    private Boolean completed = false;

    private Object source;

    public UploadEvent() {
    }

    public <T extends UploadBase> UploadEvent(T event) {
        this(event, null);
    }

    public <T extends UploadBase> UploadEvent(T event, Object source) {
        this.event = event;
        this.source = source;
    }

    public <T extends UploadBase> T getEvent(Class<T> tClass) {
        return (T) event;
    }

    public <T> T getSource(Class<T> tClass) {
        return (T) source;
    }


    public Boolean getCompleted() {
        return completed == null ? false : completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public UploadEvent completed(boolean completed) {
        setCompleted(completed);
        return this;
    }


    public <T extends UploadBase> boolean eventClass(Class<T> tClass) {
        return this.event.getClass().isAssignableFrom(tClass);
    }


}
