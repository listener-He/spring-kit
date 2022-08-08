package org.hehh.framework.gateway.routing;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 *  路由内容格式转化
 *
 * @author hehui
 * @date 2022/07/13
 */
public interface RouteContentConversation {


    /**
     * 支持格式
     *
     * @param format 格式
     * @return boolean
     */
    boolean supportFormat(String format);


    /**
     * 格式化
     *
     * @param content 内容
     * @return {@link List}<{@link RouteDefinition}>
     */
    List<RouteDefinition> format(String content);
}
