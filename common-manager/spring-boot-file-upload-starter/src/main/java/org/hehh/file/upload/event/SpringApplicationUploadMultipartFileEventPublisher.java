package org.hehh.file.upload.event;

import org.springframework.context.ApplicationEventPublisher;

/**
 * @author: HeHui
 * @date: 2021-01-27 16:51
 * @description: spring事件发布-上传单个文件
 */
public class SpringApplicationUploadMultipartFileEventPublisher implements UploadMultipartFileEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * spring应用程序上传单文件事件发布者
     *
     * @param applicationEventPublisher 应用程序事件发布者
     */
    public SpringApplicationUploadMultipartFileEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 之前
     * 保存前
     *
     * @param event 事件
     */
    @Override
    public void before(UploadMultipartFileEvent event) {
        applicationEventPublisher.publishEvent(SpringUploadMultipartFileEvent.before(this,event));
    }

    /**
     * 错误
     * 异常
     *
     * @param event     事件
     * @param exception 异常
     */
    @Override
    public void error(UploadMultipartFileEvent event, Exception exception) {
        applicationEventPublisher.publishEvent(SpringUploadMultipartFileEvent.error(this,event,exception));
    }


    /**
     * 后
     *
     * @param event 事件
     * @param url   url
     */
    @Override
    public void after(UploadMultipartFileEvent event, String url) {
        applicationEventPublisher.publishEvent(SpringUploadMultipartFileEvent.after(this,event,url));
    }
}
