package org.hehh.wechat.pay;


import org.hehh.wechat.pay.req.WxPayParam;
import org.hehh.wechat.pay.result.WxPayResult;

/**
 * @author: HeHui
 * @date: 2020-08-07 15:30
 * @description: 微信支付
 */
public interface WxPay<R extends WxPayResult,P extends WxPayParam> {


    /**
     * 支持支付
     *
     * @param appId 应用程序id
     * @param type  类型
     * @return boolean
     */
    boolean supportsPay(String appId,PayType type);




    /**
     * 支付
     *
     * @param payParam 支付参数
     * @return {@link R}
     */
    R pay(P payParam);

}
