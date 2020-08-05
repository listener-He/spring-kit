package org.hehh.cloud.spring.mvc.parameter;

import lombok.Data;

import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-07-26 17:31
 * @description: 全局限流参数
 */
@Data
public class GlobalLimitingParameter extends LimitingParameter {


    /**
     *  排除 url
     */
    private List<String> excludePatterns;


    /**
     *  必须
     */
    private List<String> urlPatterns;

}
