package org.hehh.aliyun;

import com.alipay.api.*;

/**
 * @author: HeHui
 * @date: 2020-08-22 18:48
 * @description: 阿里云签名生成器
 */
public class AliyunSignatureGenerate {

    /**
     * appId
     */
    private String appId;


    /**
     * 应用私钥
     */
    private String appRsaPrivateKey;


    /**
     * 支付宝公钥
     */
    private String payRsaPublicKey;


    /**
     * 支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
     */
    private String alipayTootCertPath;


    /**
     * 应用公钥证书路径（app_cert_path 文件绝对路径
     */
    private String appCertPath;

    /**
     * 支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
     */
    private String alipayCertPath;


    public AliyunSignatureGenerate(String appId, String appRsaPrivateKey, String payRsaPublicKey) {

        assert appId != null : "应用ID不能为空";
        assert appRsaPrivateKey != null : "应用RSA私钥不能为空";
        assert payRsaPublicKey != null : "支付宝公钥不能为空";

        this.appId = appId;
        this.appRsaPrivateKey = appRsaPrivateKey;
        this.payRsaPublicKey = payRsaPublicKey;
    }


    public AliyunSignatureGenerate(String appId, String appRsaPrivateKey, String alipayTootCertPath, String appCertPath, String alipayCertPath) {

        assert appId != null : "应用ID不能为空";
        assert appRsaPrivateKey != null : "应用RSA私钥不能为空";
        assert alipayTootCertPath != null : "支付宝CA根证书文件路径不能为空";
        assert appCertPath != null : "应用公钥证书路径不能为空";
        assert alipayCertPath != null : "支付宝公钥证书文件路径不能为空";

        this.appId = appId;
        this.appRsaPrivateKey = appRsaPrivateKey;

        this.alipayTootCertPath = alipayTootCertPath;
        this.appCertPath = appCertPath;
        this.alipayCertPath = alipayCertPath;
    }


    /**
     * 生成签名
     *
     * @param gateway
     */
    public AlipayClient keyGenerate(String gateway) {
        return new DefaultAlipayClient(gateway, appId, appRsaPrivateKey, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8, payRsaPublicKey, AlipayConstants.SIGN_TYPE_RSA2);
    }


    /**
     * 证书方式生成签名
     *
     * @param gateway
     * @return
     * @throws AlipayApiException
     */
    public AlipayClient keyFileGenerate(String gateway) throws AlipayApiException {

        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(gateway);
        certAlipayRequest.setAppId(appId);
        certAlipayRequest.setPrivateKey(appRsaPrivateKey);
        certAlipayRequest.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        certAlipayRequest.setFormat(AlipayConstants.FORMAT_JSON);
        certAlipayRequest.setCertPath(AlipayConstants.CHARSET_GBK);
        certAlipayRequest.setCertPath(appCertPath);
        certAlipayRequest.setAlipayPublicCertPath(alipayCertPath);
        certAlipayRequest.setRootCertPath(alipayTootCertPath);

        return new DefaultAlipayClient(certAlipayRequest);
    }
}
