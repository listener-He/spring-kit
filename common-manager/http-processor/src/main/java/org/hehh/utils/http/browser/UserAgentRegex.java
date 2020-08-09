package org.hehh.utils.http.browser;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-10 00:07
 * @description: UserAgent 匹配
 */
@Data
public class UserAgentRegex {

    private String name;

    private String regex;
}
