package org.hehh.wechat.pay;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.hehh.wechat.pay.req.WxAPPPayParam;
import org.hehh.wechat.pay.result.WxAPPPayResult;

/**
 * @author: HeHui
 * @date: 2020-08-07 17:21
 * @description: app支付
 */
public class WxAPPPay extends AbstractBinarywangWxPay<WxAPPPayResult, WxAPPPayParam> {




    public WxAPPPay(WxPayService wxPayService) {
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
        return PayType.APP.equals(type);
    }




    /**
     * 支付
     *
     * @param payParam 支付参数
     * @return {@link WxAPPPayParam}
     */
    @Override
    public WxAPPPayResult pay(WxAPPPayParam payParam) {


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
            WxPayAppOrderResult result = wxPayService.createOrder(request);

            WxAPPPayResult payResult = new WxAPPPayResult();
            payResult.setPrepayId(result.getPrepayId());
            payResult.setPartnerId(result.getPartnerId());
            payResult.setReturnCode("0");
            payResult.setAppId(result.getAppId());
            payResult.setNonceStr(result.getNonceStr());
            payResult.setSign(result.getSign());
            payResult.setPackageValue(result.getPackageValue());
            payResult.setTimeStamp(result.getTimeStamp());
            return payResult;
        } catch (WxPayException e) {
            WxAPPPayResult result = new WxAPPPayResult();
            result.setErrcode(e.getErrCode());
            result.setErrmsg(e.getErrCodeDes());
            return result;
        }
    }
}
