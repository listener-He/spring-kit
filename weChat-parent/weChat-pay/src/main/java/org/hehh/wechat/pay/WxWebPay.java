package org.hehh.wechat.pay;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.hehh.wechat.pay.req.WxAPPPayParam;
import org.hehh.wechat.pay.req.WxNativePayParam;
import org.hehh.wechat.pay.req.WxPayParam;
import org.hehh.wechat.pay.req.WxWebPayParam;
import org.hehh.wechat.pay.result.WxNativePayResult;
import org.hehh.wechat.pay.result.WxWebPayResult;

import java.net.HttpCookie;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: HeHui
 * @date: 2020-08-07 17:21
 * @description: 网页支付
 */
public class WxWebPay extends AbstractBinarywangWxPay<WxWebPayResult, WxWebPayParam> {




    public WxWebPay(WxPayService wxPayService) {
        super(wxPayService);
    }




    /**
     *
     * 支持类型
     *
     * @param type 类型
     * @return boolean
     */
    @Override
    boolean supportsType(PayType type) {
        return PayType.H5.equals(type);
    }




    /**
     * 支付
     *
     * @param payParam 支付参数
     * @return {@link WxAPPPayParam}
     */
    @Override
    public WxWebPayResult pay(WxWebPayParam payParam) {


        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setNotifyUrl(payParam.getNotifyUrl());
        request.setTradeType(payParam.getTradeType());
        request.setVersion(payParam.getVersion());
        request.setDeviceInfo(payParam.getDeviceInfo());
        request.setBody(payParam.getBody());
        request.setDetail(payParam.getDetail());
        request.setAttach(payParam.getAttach());
        request.setOutTradeNo(payParam.getOutTradeNo());
        request.setFeeType(payParam.getFeeType());
        request.setTotalFee(payParam.getTotalFee());
        request.setSpbillCreateIp(payParam.getSpbillCreateIp());
        request.setTimeStart(payParam.getTimeStart());
        request.setTimeExpire(payParam.getTimeExpire());
        request.setGoodsTag(payParam.getGoodsTag());
        request.setProductId(payParam.getProductId());
        request.setLimitPay(payParam.getLimitPay());
        request.setOpenid(payParam.getOpenid());
        request.setSubOpenid(payParam.getSubAppId());
        request.setReceipt(payParam.getReceipt());
        request.setSceneInfo(payParam.getSceneInfo());
        request.setFingerprint(payParam.getFingerprint());
        request.setProfitSharing(payParam.getProfitSharing());
        request.setWorkWxSign(payParam.getWorkWxSign());
        request.setAppid(payParam.getAppId());
        request.setMchId(payParam.getMchId());
        request.setNonceStr(payParam.getNonceStr());
        request.setSubAppId(payParam.getSubAppId());
        request.setSubMchId(payParam.getSubMchId());
        request.setSign(payParam.getSign());
        request.setSignType(payParam.getSignType());


        try {
            WxPayMwebOrderResult result = wxPayService.createOrder(request);

            WxWebPayResult payResult = new WxWebPayResult();
            payResult.setReturnCode("0");
            payResult.setMwebUrl(result.getMwebUrl());
            return payResult;
        } catch (WxPayException e) {
            WxWebPayResult result = new WxWebPayResult();
            result.setErrCode(e.getErrCode());
            result.setErrCodeDes(e.getErrCodeDes());
            return result;
        }
    }


}
