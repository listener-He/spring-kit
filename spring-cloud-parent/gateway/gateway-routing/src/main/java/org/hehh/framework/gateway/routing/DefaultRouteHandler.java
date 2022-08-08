package org.hehh.framework.gateway.routing;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 默认路由处理器
 *
 * @author HeHui
 * @date 2022-03-24 22:11
 */
public class DefaultRouteHandler implements RouteHandler {

    private final RouteDefinitionRepository repository;

    public DefaultRouteHandler(RouteDefinitionRepository repository) {
        this.repository = repository;
    }

    /**
     * 添加路由
     *
     * @param routeId    路线id
     * @param url        url
     * @param predicates 谓词
     * @param filters    过滤器
     * @return {@link Mono}<{@link Void}>
     */
    @Override
    public Mono<Void> put(String routeId, String url, List<String> predicates, List<String> filters) {
        return null;
    }

    /**
     * 删除
     *
     * @param routeId    路线id
     * @param url        url
     * @param predicates 谓词
     * @param filters    过滤器
     * @return {@link Mono}<{@link Void}>
     */
    @Override
    public Mono<Void> delete(String routeId, String url, List<String> predicates, List<String> filters) {
        return null;
    }

    /**
     * 获取
     *
     * @param routeId 路线id
     * @return {@link Mono}<{@link RouteDefinition}>
     */
    @Override
    public Mono<RouteDefinition> get(String routeId) {
        return null;
    }

    /**
     * 刷新
     *
     * @return {@link Mono}<{@link Void}>
     */
    @Override
    public Mono<Void> refresh() {
        return null;
    }
}
