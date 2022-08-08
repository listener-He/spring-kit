package org.hehh.framework.gateway.predicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 媒体类型 路由谓词工厂
 *
 * @author HeHui
 * @date 2021-06-06 18:40
 */
public class MediaTypeRoutePredicateFactory extends AbstractRoutePredicateFactory<MediaTypeRoutePredicateFactory.Config> {


    public static final String TYPE_KEY = "mediaType";

    public static final List<String> ATTR_SORT = Arrays.asList(TYPE_KEY);


    public MediaTypeRoutePredicateFactory() {
        super(Config.class);
    }


    /**
     * Returns hints about the number of args and the order for shortcut parsing.
     *
     * @return the list of hints
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return ATTR_SORT;
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
            if (mediaType == null) {
                return false;
            }
            MediaType configMediaType = config.getMediaType();
            return configMediaType == null || configMediaType.includes(mediaType);

        };
    }

    public static class Config {

        private MediaType mediaType;

        public MediaType getMediaType() {
            return mediaType;
        }

        public void setMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
        }
    }
}
