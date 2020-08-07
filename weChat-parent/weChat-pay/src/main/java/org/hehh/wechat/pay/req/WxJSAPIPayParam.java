package org.hehh.wechat.pay.req;


import lombok.Getter;

/**
 * @author: HeHui
 * @date: 2020-08-07 16:28
 * @description: 微信mp支付参数
 */
public class WxJSAPIPayParam extends WxUnifiedorderPayParam {


    @Getter
    private  String tradeType = "JSAPI";

}
