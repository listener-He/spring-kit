package org.hehh.cloud.spring.mvc.config;

import org.hehh.cloud.spring.mvc.core.IHandlerMethodArgumentResolverAdapter;

import java.util.List;

/**
 * @author: HeHui
 * @create: 2020-03-23 01:03
 * @description: 请求参数解析器适配器配置类
 **/
public interface ResolverAdapterConfiguration {


    /**
     *  添加请求参数解析器适配器
     * @param resolverAdapters
     */
    void addResolverAdapters(List<IHandlerMethodArgumentResolverAdapter> resolverAdapters);
}
