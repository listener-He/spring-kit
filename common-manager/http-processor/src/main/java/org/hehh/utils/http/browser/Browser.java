package org.hehh.utils.http.browser;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-10 00:06
 * @description: 浏览器
 */
@Data
public class Browser extends UserAgentRegex {

    private String versionRegex;
}
