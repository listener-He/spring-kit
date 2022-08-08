package org.hehh.framework.gateway.ratelimit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: HeHui
 * @date: 2020-06-12 16:00
 * @description: 用户token 限流key解析器
 */
public class TokenKeyResolver implements KeyResolver {

    /**
     *  bean-name（只是声明不绝对）
     */
    public static final String BEAN_NAME = "tokenKeyResolver";

    /**
     *  请求头的名称
     */
    private final String headName;

    public TokenKeyResolver(String headName){
        this.headName = headName;
    }


    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(headName));
    }
}
