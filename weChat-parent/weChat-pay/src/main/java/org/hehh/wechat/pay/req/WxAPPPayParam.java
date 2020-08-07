package org.hehh.wechat.pay.req;


import lombok.Getter;

/**
 * @author: HeHui
 * @date: 2020-08-07 16:28
 * @description: 微信app支付参数
 */
public class WxAPPPayParam extends WxUnifiedorderPayParam {


    @Getter
    private  String tradeType = "APP";

}
