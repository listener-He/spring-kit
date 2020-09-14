package org.hehh.cloud.spring.mvc.parameter;

import lombok.Data;
import org.hehh.cloud.spring.mvc.sign.SignatureModel;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-09-15 00:51
 * @description: 签名配置参数
 */
@Data
@ConfigurationProperties(prefix = "spring.request.signature")
public class SignatureParameter {

    /**
     * 模型
     */
    private SignatureModel model = SignatureModel.HMAC_SHA256;


    /**
     * 过期时间
     */
    private long overdueTime = 1000 * 60 * 10;

    /**
     * 签名的名字
     */
    private  String signName = "Sign";

    /**
     * 时间撮的名字
     */
    private  String timeName = "Timestamp";

    /**
     * 应用id参数名称
     */
    private  String appName = "APP-ID";


    /**
     * 忽略
     */
    private String[] ignore;


    /**
     * 应用程序的密钥
     */
    private Map<String,String> appSecret;
}
