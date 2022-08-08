package org.hehh.framework.gateway.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hehh.framework.gateway.routing.nacos.NacosVersionRoundRobinLoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 版本号权重路由过滤
 *
 * @author HeHui
 * @date 2022-07-15 13:28
 */
public class VersionReactiveLoadBalancerClientFilter implements GlobalFilter, Ordered {

    private final Log log = LogFactory.getLog(VersionReactiveLoadBalancerClientFilter.class);

    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;

    private final LoadBalancerClientFactory clientFactory;

    private final String scheme = "lbs";

    public VersionReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix =  exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
        if (url != null && (scheme.equals(url.getScheme()) || scheme.equals(schemePrefix))) {
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);
            if (log.isTraceEnabled()) {
                log.trace(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);
            }
            return this.choose(exchange).doOnNext((response) -> {
                if (!response.hasServer()) {
                    throw NotFoundException.create(true, "Unable to find instance for " + url.getHost());
                } else {
                    URI uri = exchange.getRequest().getURI();
                    String overrideScheme = null;
                    if (schemePrefix != null) {
                        overrideScheme = url.getScheme();
                    }

                    DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(response.getServer(), overrideScheme);
                    URI requestUrl = this.reconstructURI(serviceInstance, uri);
                    if (log.isTraceEnabled()) {
                        log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                    }
                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
                }
            }).then(chain.filter(exchange));
        } else {
            return chain.filter(exchange);
        }
    }


    private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        ReactorServiceInstanceLoadBalancer loadBalancer = new NacosVersionRoundRobinLoadBalancer(clientFactory.getLazyProvider(uri.getHost(), ServiceInstanceListSupplier.class), uri.getHost(), null);
        return loadBalancer.choose(this.createRequest(exchange));
    }

    private Request createRequest(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return new DefaultRequest<>(headers);
    }

    protected URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }


    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }
}
