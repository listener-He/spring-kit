package org.hehh.file.upload.event;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2021-01-28 14:24
 * @description: 上传拦截器组合
 */
public class UploadInterceptorChain implements UploadFilterChain {

    private final List<UploadInterceptor> interceptors;

    /**
     *
     */
    private int index = 0;

    protected UploadInterceptorChain(Collection<UploadInterceptor> interceptors) {
        this(interceptors.stream().collect(Collectors.toList()));
    }

    protected UploadInterceptorChain(List<UploadInterceptor> interceptors) {
        interceptors.sort(Comparator.comparing(UploadInterceptor::getOrder));
        this.interceptors = interceptors;
    }


    /**
     * 过滤器
     *
     * @param event 事件
     */
    @Override
    public void doFilter(UploadEvent event) throws IOException {
        UploadInterceptor[] array = interceptors.stream().filter(v -> v.supports(event)).toArray(UploadInterceptor[]::new);
        if (array == null || array.length < 1) {
            return;
        }

        //TODO 没想好怎么做...


    }


}
