package org.hehh.framework.gateway.routing;

import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 路由处理程序
 *
 * @author hehui
 * @date 2022/03/24
 */
public interface RouteHandler {

    /**
     *  添加路由
     *
     * @param routeId    路线id
     * @param url        url
     * @param predicates 谓词
     * @param filters    过滤器
     * @return {@link Mono}<{@link Void}>
     */
    Mono<Void> put(String routeId, String url, List<String> predicates, List<String> filters);


    /**
     * 删除
     *
     * @param routeId    路线id
     * @param url        url
     * @param predicates 谓词
     * @param filters    过滤器
     * @return {@link Mono}<{@link Void}>
     */
    Mono<Void> delete(String routeId, String url, List<String> predicates, List<String> filters);


    /**
     *  获取
     *
     * @param routeId 路线id
     * @return {@link Mono}<{@link RouteDefinition}>
     */
    Mono<RouteDefinition> get(String routeId);


    /**
     * 刷新
     *
     * @return {@link Mono}<{@link Void}>
     */
    Mono<Void> refresh();

}
