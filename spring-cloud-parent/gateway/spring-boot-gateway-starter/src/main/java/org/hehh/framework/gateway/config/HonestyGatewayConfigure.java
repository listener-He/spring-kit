package org.hehh.framework.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hehh.framework.gateway.filters.global.CorsResponseHeaderFilter;
import org.hehh.framework.gateway.handler.JsonExceptionHandler;
import org.hehh.framework.gateway.predicate.EquipmentRoutePredicateFactory;
import org.hehh.framework.gateway.predicate.MediaTypeRoutePredicateFactory;
import org.hehh.framework.gateway.ratelimit.RemoteAddressKeyResolver;
import org.hehh.framework.gateway.ratelimit.TokenKeyResolver;
import org.hehh.framework.gateway.routing.JsonRouteContentConversation;
import org.hehh.framework.gateway.routing.RouteContentConversation;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * 网关配置
 *
 * @author HeHui
 * @date 2022-07-13 22:37
 */
@Configuration
// @EnableConfigurationProperties({ServerProperties.class, WebProperties.class})
public class HonestyGatewayConfigure {


//    /**
//     * 错误网络异常处理程序
//     *
//     * @param serverProperties      服务器属性
//     * @param applicationContext    应用程序上下文
//     * @param errorAttributes       错误属性
//     * @param viewResolversProvider 视图解析器供应商
//     * @param webProperties         网络属性
//     * @return {@link ErrorWebExceptionHandler}
//     */
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @ConditionalOnMissingBean(JsonExceptionHandler.class)
//    public ErrorWebExceptionHandler errorWebExceptionHandler(ServerProperties serverProperties, ApplicationContext applicationContext, ErrorAttributes errorAttributes, ObjectProvider<List<ViewResolver>> viewResolversProvider, WebProperties webProperties) {
//        JsonExceptionHandler exceptionHandler = new JsonExceptionHandler(
//            errorAttributes,
//            webProperties.getResources(),
//            serverProperties.getError(),
//            applicationContext);
//        ServerCodecConfigurer serverCodecConfigurer = new DefaultServerCodecConfigurer();
//        exceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
//        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
//        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
//        return exceptionHandler;
//    }

    /**
     * 设备谓词工厂
     *
     * @return {@link EquipmentRoutePredicateFactory}
     */
    @Bean
    @ConditionalOnMissingBean(EquipmentRoutePredicateFactory.class)
    public EquipmentRoutePredicateFactory equipmentRoutePredicateFactory() {
        return new EquipmentRoutePredicateFactory();
    }

    /**
     * 媒体类型谓词工厂
     *
     * @return {@link MediaTypeRoutePredicateFactory}
     */
    @Bean
    @ConditionalOnMissingBean(MediaTypeRoutePredicateFactory.class)
    public MediaTypeRoutePredicateFactory mediaTypeRoutePredicateFactory() {
        return new MediaTypeRoutePredicateFactory();
    }


    /**
     * 响应头过滤
     *
     * @return {@link CorsResponseHeaderFilter}
     */
    @Bean
    @ConditionalOnMissingBean(CorsResponseHeaderFilter.class)
    public CorsResponseHeaderFilter corsResponseHeaderFilter() {
        return new CorsResponseHeaderFilter();
    }


    /**
     * 远程地址关键解析器
     *
     * @return {@link RemoteAddressKeyResolver}
     */
    @Bean(RemoteAddressKeyResolver.BEAN_NAME)
    @ConditionalOnMissingBean(RemoteAddressKeyResolver.class)
    @Primary
    public RemoteAddressKeyResolver remoteAddressKeyResolver() {
        return new RemoteAddressKeyResolver();
    }


    /**
     * 令牌密钥分解器
     *
     * @return {@link TokenKeyResolver}
     */
    @Bean
    @ConditionalOnMissingBean(TokenKeyResolver.class)
    public TokenKeyResolver tokenKeyResolver() {
        return new TokenKeyResolver(HttpHeaders.AUTHORIZATION);
    }


    @Bean
    @ConditionalOnMissingBean(RouteContentConversation.class)
    @ConditionalOnProperty(prefix = "spring.cloud.gateway.route", name = "format", havingValue = JsonRouteContentConversation.JSON_FORMAT, matchIfMissing = true)
    public JsonRouteContentConversation jsonRouteContentConversation() {
        return new JsonRouteContentConversation(new ObjectMapper());
    }



}
