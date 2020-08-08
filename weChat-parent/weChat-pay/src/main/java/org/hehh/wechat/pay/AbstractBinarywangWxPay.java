package org.hehh.wechat.pay;

import com.github.binarywang.wxpay.service.WxPayService;
import org.hehh.wechat.pay.req.WxPayParam;
import org.hehh.wechat.pay.result.WxPayResult;

/**
 *  <pre>
 *      基于binarywang的微信支付
 *      http://binary.ac.cn/weixin-java-pay-javadoc/
 *  </pre>
 * @author: HeHui
 * @date: 2020-08-07 17:25
 * @description: 微信支付抽象类
 *
 */
public abstract class AbstractBinarywangWxPay<R extends WxPayResult,P extends WxPayParam> implements WxPay<R,P> {

    protected final WxPayService wxPayService;

    protected AbstractBinarywangWxPay(WxPayService wxPayService) {
        this.wxPayService = wxPayService;
    }


    /**
     * 支持支付
     *
     * @param appId 应用程序id
     * @param type  类型
     * @return boolean
     */
    @Override
    public boolean supportsPay(String appId, PayType type) {
        return wxPayService.getConfig().getAppId().equals(appId) && supportsType(type);
    }


    /**
     * 支持类型
     *
     * @param type 类型
     * @return boolean
     */
    abstract boolean supportsType(PayType type);
}
