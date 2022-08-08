package org.hehh.framework.gateway.predicate;

import org.hehh.framework.gateway.support.ContextConstant;
import org.hehh.util.core.StringUtil;
import org.hehh.util.http.UserAgent;
import org.hehh.util.http.UserAgentResolving;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 设备路由谓词工厂
 *
 * @author HeHui
 * @date 2021-06-06 18:46
 */
public class EquipmentRoutePredicateFactory extends AbstractRoutePredicateFactory<EquipmentRoutePredicateFactory.Config> {


    /**
     * 设备关键
     */
    public static final String EQUIPMENT_KEY = "equipment";
    /**
     * 手机
     */
    public static final String MOBILE_KEY = "mobile";

    private static final List<String> ATTR_SORT = Arrays.asList(EQUIPMENT_KEY, MOBILE_KEY);

    public EquipmentRoutePredicateFactory() {
        super(Config.class);
    }


    /**
     * 快捷键字段顺序
     *
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return ATTR_SORT;
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {

        return exchange -> {
            String userAgent = exchange.getRequest().getHeaders().getFirst(HttpHeaders.USER_AGENT);
            if (StringUtil.isBlank(userAgent)) {
                return false;
            }
            UserAgent parse = UserAgentResolving.parse(userAgent);
            if (parse == null) {
                return false;
            }
            exchange.getAttributes().put(ContextConstant.USER_AGENT_PARSE, parse);
            if (config.getMobile() != null && !Objects.equals(config.getMobile(), parse.getMobile())) {
                return false;
            }
            if (StringUtil.isNotBlank(config.getEquipment()) && config.getEquipment().equals("*")) {
                return true;
            }
            String name = Optional.of(parse).filter(UserAgent::getMobile).map(UserAgent::getSystem).orElseGet(() -> parse.getBrowser());

            return name.equalsIgnoreCase(config.getEquipment()) || StringUtil.isRegular(name, config.getEquipment());

        };
    }


    public static class Config {

        /**
         * 设备
         */
        private String equipment;
        /**
         *  是否手机端
         */
        private Boolean mobile;

        public Boolean getMobile() {
            return mobile;
        }

        public void setMobile(Boolean mobile) {
            this.mobile = mobile;
        }

        public String getEquipment() {
            return equipment;
        }

        public void setEquipment(String equipment) {
            this.equipment = equipment;
        }
    }
}
