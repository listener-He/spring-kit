package org.hehh.cloud.spring.userAgent;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-10 00:54
 * @description: 浏览器
 */
@Data
public class Browser extends UserAgentRegex {

    private String versionRegex;
}
