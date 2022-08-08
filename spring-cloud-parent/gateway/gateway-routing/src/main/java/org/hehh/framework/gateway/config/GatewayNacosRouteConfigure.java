package org.hehh.framework.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import org.hehh.framework.gateway.filters.VersionReactiveLoadBalancerClientFilter;
import org.hehh.framework.gateway.routing.RouteContentConversation;
import org.hehh.framework.gateway.routing.nacos.NacosRouteDefinitionLocator;
import org.hehh.framework.gateway.routing.nacos.NacosRouteProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * gateway nacos 路由配置
 *
 * @author HeHui
 * @date 2022-07-13 22:43
 */
@Configuration
@EnableConfigurationProperties(NacosRouteProperties.class)
@ConditionalOnClass(NacosConfigManager.class)
public class GatewayNacosRouteConfigure {


    /**
     * nacos 路由定义定位器
     *
     * @param properties               属性
     * @param nacosConfigManager       nacos配置管理器
     * @param routeContentConversation 路由转换
     * @return {@link NacosRouteDefinitionLocator}
     * @throws IllegalAccessException 非法访问异常
     */
    @Bean
    @ConditionalOnMissingBean(NacosRouteDefinitionLocator.class)
    public NacosRouteDefinitionLocator nacosRouteDefinitionLocator(NacosRouteProperties properties, NacosConfigManager nacosConfigManager, RouteContentConversation routeContentConversation) throws IllegalAccessException {
        return new NacosRouteDefinitionLocator(properties.getDataId(), properties.getGroupId(), properties.getTimeoutMs(), nacosConfigManager, routeContentConversation);
    }

    /**
     * 版本负载均衡器 过滤器
     *
     * @param loadBalancerClientFactory 负载均衡器客户工厂
     * @return {@link VersionReactiveLoadBalancerClientFilter}
     */
    @Bean
    @ConditionalOnMissingBean(VersionReactiveLoadBalancerClientFilter.class)
    public VersionReactiveLoadBalancerClientFilter versionReactiveLoadBalancerClientFilter(LoadBalancerClientFactory loadBalancerClientFactory) {
        return new VersionReactiveLoadBalancerClientFilter(loadBalancerClientFactory);
    }
}
