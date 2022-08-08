package org.hehh.framework.gateway.routing.nacos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * nacos 路由配置参数
 *
 * @author HeHui
 * @date 2022-07-13 22:39
 */
@ConfigurationProperties(NacosRouteProperties.PREFIX)
public class NacosRouteProperties implements Serializable {

    public static final String PREFIX = "spring.cloud.gateway.route.nacos";
    private static final long serialVersionUID = -5653028796854957079L;

    /**
     * 数据标识
     */
    private String dataId;

    /**
     * 组id
     */
    private String groupId;

    /**
     * 超时毫秒
     */
    private Integer timeoutMs = 10000;


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(Integer timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
}
