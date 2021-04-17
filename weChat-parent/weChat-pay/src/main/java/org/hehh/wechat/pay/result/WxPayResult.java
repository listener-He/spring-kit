package org.hehh.wechat.pay.result;

import lombok.Data;
import org.hehh.weChat.result.WxResult;

import java.io.Serializable;

/**
 * @author: HeHui
 * @date: 2020-08-07 15:55
 * @description: 微信付款返回
 */
@Data
public class WxPayResult extends WxResult {

    /**
     * 返回状态码.
     */
    protected String returnCode;
    /**
     * 返回信息.
     */
    protected String returnMsg;

    //当return_code为SUCCESS的时候，还会包括以下字段：

    /**
     * 业务结果.
     */
    private String resultCode;


    /**
     * 公众账号ID.
     */
    private String appId;
    /**
     * 商户号.
     */
    private String mchId;
    /**
     * 服务商模式下的子公众账号ID.
     */
    private String subAppId;
    /**
     * 服务商模式下的子商户号.
     */
    private String subMchId;
    /**
     * 随机字符串.
     */
    private String nonceStr;
    /**
     * 签名.
     */
    private String sign;



    /**
     * 响应是否成功
     *
     * @return
     */
    @Override
    public boolean ok() {
        return super.ok() && (resultCode != null && resultCode.equalsIgnoreCase("SUCCESS"));
    }
}
