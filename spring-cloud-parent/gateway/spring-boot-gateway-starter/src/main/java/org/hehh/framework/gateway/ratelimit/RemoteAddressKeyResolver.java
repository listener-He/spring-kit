package org.hehh.framework.gateway.ratelimit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: HeHui
 * @date: 2020-06-12 16:04
 * @description: IP 限流key解析器
 */
public class RemoteAddressKeyResolver implements KeyResolver {


    /**
     *  bean-name（只是声明不绝对）
     */
    public static final String BEAN_NAME = "ipKeyResolver";

    private final RemoteAddressResolver addressResolver = XForwardedRemoteAddressResolver.maxTrustedIndex(10);


    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.justOrEmpty(addressResolver.resolve(exchange).getAddress().getHostAddress());
    }
}
