package org.hehh.file.upload;

import org.hehh.file.upload.event.*;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2021-01-28 14:24
 * @description: 上传拦截器组合
 */
public class UploadInterceptorComposite implements UploadInterceptor {

    private final List<UploadInterceptor> interceptors;

    protected UploadInterceptorComposite(Collection<UploadInterceptor> interceptors) {
        this(interceptors.stream().collect(Collectors.toList()));
    }

    protected UploadInterceptorComposite(List<UploadInterceptor> interceptors) {
        interceptors.sort(Comparator.comparing(UploadInterceptor::getOrder));
        this.interceptors = interceptors;
    }


    /**
     * 支持
     *
     * @param event 事件
     *
     * @return boolean
     */
    @Override
    public boolean supports(UploadEvent event) {
        if (CollectionUtils.isEmpty(interceptors)) {
            return false;
        }
        for (UploadInterceptor interceptor : interceptors) {
            if (interceptor.supports(event)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 之前
     * 保存前
     *
     * @param event 事件
     */
    @Override
    public boolean before(UploadEvent event) {
        AtomicBoolean temp = new AtomicBoolean(true);
        interceptors.stream().filter(v -> v.supports(event)).forEach(interceptor -> {
            if (!interceptor.before(event)) {
                temp.set(false);
                return;
            }
        });
        return temp.get();
    }

    /**
     * 错误
     * 异常
     *
     * @param event     事件
     * @param exception 异常
     */
    @Override
    public void error(UploadEvent event, Exception exception) {
        interceptors.stream().filter(v -> v.supports(event)).forEach(interceptor -> {
            interceptor.error(event, exception);
        });
    }

    /**
     * 后
     *
     * @param event 事件
     * @param url   url
     */
    @Override
    public void after(UploadEvent event, String url) {
        interceptors.stream().filter(v -> v.supports(event)).forEach(interceptor -> {
            interceptor.after(event, url);
        });
    }
}
