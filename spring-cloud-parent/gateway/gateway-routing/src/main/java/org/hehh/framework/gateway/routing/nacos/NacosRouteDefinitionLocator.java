package org.hehh.framework.gateway.routing.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hehh.framework.gateway.routing.RouteContentConversation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * nacos 路由
 *
 * @author HeHui
 * @date 2022-07-13 21:52
 */
public class NacosRouteDefinitionLocator implements RouteDefinitionLocator, ApplicationContextAware {

    private final Log log = LogFactory.getLog(NacosRouteDefinitionLocator.class);

    private final String dataId;

    private final String groupId;

    private final int timeoutMs;

    private final NacosConfigManager nacosConfigManager;

    private final List<RouteDefinition> routes = Collections.synchronizedList(new ArrayList<>());

    private final RouteContentConversation routeContentConversation;

    public NacosRouteDefinitionLocator(String dataId, String groupId, int timeoutMs, NacosConfigManager nacosConfigManager, RouteContentConversation routeContentConversation) throws IllegalAccessException {
        this.dataId = dataId;
        this.groupId = groupId;
        this.timeoutMs = timeoutMs;
        this.nacosConfigManager = nacosConfigManager;
        this.routeContentConversation = routeContentConversation;
        this.reload(null);
    }


    /**
     * 重新加载
     *
     * @param content 内容
     * @throws IllegalAccessException 非法访问异常
     * @throws NacosException         nacos的异常
     */
    private void reload(String content) throws IllegalAccessException {
        List<RouteDefinition> configRoutes = routeContentConversation.format(Optional.ofNullable(content).orElseGet(() ->
            {
                try {
                    return nacosConfigManager.getConfigService().getConfig(dataId, groupId, timeoutMs);
                } catch (NacosException e) {
                    log.error("[路由配置] reload gateway nacos route config error", e);
                    return null;
                }
            }
        ));
        if (!CollectionUtils.isEmpty(configRoutes)) {
            if (configRoutes.stream().anyMatch(r -> !StringUtils.hasLength(r.getId()))) {
                throw new IllegalAccessException("please configure router id");
            }
            this.routes.clear();
            this.routes.addAll(configRoutes);
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(Collections.unmodifiableList(routes));
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            nacosConfigManager.getConfigService().addListener(dataId, groupId, new Listener() {

                /**
                 * Get executor for execute this receive.
                 *
                 * @return Executor
                 */
                @Override
                public Executor getExecutor() {
                    return null;
                }

                /**
                 * Receive config info.
                 *
                 * @param configInfo config info
                 */
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("[路由配置] receive gateway nacos route config, info: " + configInfo);
                    try {
                        reload(configInfo);
                    } catch (Exception e) {
                        log.error("[路由配置] receive gateway nacos route reload format error", e);
                    }
                    //如果节点变化就发送RefreshRoutesEvent事件。
                    applicationContext.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            log.error("[路由配置] receive gateway nacos route listener error", e);
        }
    }
}
