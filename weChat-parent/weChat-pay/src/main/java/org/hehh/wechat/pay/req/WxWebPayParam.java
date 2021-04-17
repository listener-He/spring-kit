package org.hehh.wechat.pay.req;


import lombok.Getter;

/**
 * @author: HeHui
 * @date: 2020-08-07 16:28
 * @description: 微信网页支付支付参数
 */
public class WxWebPayParam extends WxUnifiedorderPayParam {


    @Getter
    private  String tradeType = "MWEB";

}
