package org.hehh.wechat.pay;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.hehh.wechat.pay.req.WxAPPPayParam;
import org.hehh.wechat.pay.req.WxJSAPIPayParam;
import org.hehh.wechat.pay.result.WxJSAPIPayResult;

/**
 * @author: HeHui
 * @date: 2020-08-07 17:21
 * @description: jsapi支付
 */
public class WxJSAPIPay extends AbstractBinarywangWxPay<WxJSAPIPayResult, WxJSAPIPayParam> {




    public WxJSAPIPay(WxPayService wxPayService) {
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
        return PayType.JSAPI.equals(type);
    }




    /**
     * 支付
     *
     * @param payParam 支付参数
     * @return {@link WxAPPPayParam}
     */
    @Override
    public WxJSAPIPayResult pay(WxJSAPIPayParam payParam) {


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
            WxPayMpOrderResult result = wxPayService.createOrder(request);

            WxJSAPIPayResult payResult = new WxJSAPIPayResult();
            payResult.setReturnCode("0");
            payResult.setAppId(result.getAppId());
            payResult.setNonceStr(result.getNonceStr());
            payResult.setPaySign(result.getPaySign());
            payResult.setSignType(result.getSignType());
            payResult.setNonceStr(result.getNonceStr());
            payResult.setPackageValue(result.getPackageValue());
            payResult.setTimeStamp(result.getTimeStamp());
            return payResult;
        } catch (WxPayException e) {
            WxJSAPIPayResult result = new WxJSAPIPayResult();
            result.setErrCode(e.getErrCode());
            result.setErrCodeDes(e.getErrCodeDes());
            return result;
        }
    }
}
