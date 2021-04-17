package org.hehh.wechat;

import lombok.Data;
import org.hehh.weChat.WxConfigurationParameter;

/**
 * @author: HeHui
 * @date: 2020-08-07 11:51
 * @description: 微信支付配置参数
 */
@Data
public class WxPayConfigurationParameter extends WxConfigurationParameter {


    /**
     *  支付基础接口
     */
    private String payBaseUrl;

    /**
     * http请求连接超时时间.
     */
    private int httpConnectionTimeout = 5000;


    /**
     * http请求数据读取等待时间.
     */
    private int httpTimeout = 10000;


    /**
     * 商户号.
     */
    private String mchId;

    /**
     * 签名方式.
     * 有两种HMAC_SHA256 和MD5
     */
    private String signType;


    /**
     * p12证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String keyPath;

    /**
     * apiclient_key.pem证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String privateKeyPath;
    /**
     * apiclient_cert.pem证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String privateCertPath;


    /**
     * 微信支付异步回掉地址，通知url必须为直接可访问的url，不能携带参数.
     */
    private String notifyUrl;



    /**
     * 微信支付是否使用仿真测试环境.
     * 默认不使用
     */
    private boolean useSandboxEnv = false;


    /**
     * apiV3 秘钥值.
     */
    private String apiV3Key;
}
