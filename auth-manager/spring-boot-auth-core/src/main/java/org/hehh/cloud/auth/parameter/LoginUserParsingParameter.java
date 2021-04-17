package org.hehh.cloud.auth.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: HeHui
 * @date: 2020-09-16 01:07
 * @description: 登陆用户解析参数
 */
@Data
@ConfigurationProperties(prefix = "token.arsing")
public class LoginUserParsingParameter {

    /**
     * 令牌名称
     */
    private String tokenName = "access_token";


}
