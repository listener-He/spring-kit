package org.hehh.utils.http.config;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-09 17:49
 * @description: http代理配置参数
 */
@Data
public class HttpProxyConfigurationParameters {

    /**
     *  最大请求池
     */
    private Integer max = Integer.MAX_VALUE;
}
