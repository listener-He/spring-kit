package org.hehh.framework.gateway.filters.global;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * @Author : HeHui
 * @Date : 2020/7/22 13:44
 * @Describe :跨域返回header去重过滤器
 **/
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        /*
         * 指定此过滤器位于NettyWriteResponseFilter之后
         * 即待处理完响应体后接着处理响应头
         */
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.defer(() -> {
            exchange.getResponse().getHeaders().entrySet().stream()
                .filter(handler -> (handler.getKey().equalsIgnoreCase(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                    || handler.getKey().equalsIgnoreCase(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)
                ))
                .filter(handler -> (handler.getValue() != null && handler.getValue().size() > 1))
                .forEach(kv ->
                {
                    ArrayList<String> values = new ArrayList<>(1);
                    values.add(kv.getValue().get(0));
                    kv.setValue(values);
                });

            return chain.filter(exchange);
        }));
    }
}
