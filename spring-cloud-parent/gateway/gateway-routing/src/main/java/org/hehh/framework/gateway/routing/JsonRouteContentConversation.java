package org.hehh.framework.gateway.routing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hehh.util.core.StringUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * json格式路由转换
 *
 * @author HeHui
 * @date 2022-07-13 22:27
 */
public class JsonRouteContentConversation implements RouteContentConversation {

    public static final String JSON_FORMAT = "json";

    private final ObjectMapper objectMapper;

    public JsonRouteContentConversation(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 支持格式
     *
     * @param format 格式
     * @return boolean
     */
    @Override
    public boolean supportFormat(String format) {
        return JSON_FORMAT.equalsIgnoreCase(format);
    }

    /**
     * 格式化
     *
     * @param content 内容
     * @return {@link List}<{@link RouteDefinition}>
     */
    @Override
    public List<RouteDefinition> format(String content) {
        if (!StringUtil.isJsonArray(content)) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, RouteDefinition.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
