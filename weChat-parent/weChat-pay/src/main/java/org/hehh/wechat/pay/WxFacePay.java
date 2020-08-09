package org.hehh.wechat.pay;

import com.github.binarywang.wxpay.bean.request.WxPayFacepayRequest;
import com.github.binarywang.wxpay.bean.result.WxPayFacepayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.hehh.wechat.pay.req.WxFacePayParam;
import org.hehh.wechat.pay.result.WxFacePayResult;

/**
 * @author: HeHui
 * @date: 2020-08-07 18:30
 * @description: 微信刷脸支付
 */
public class WxFacePay extends AbstractBinarywangWxPay<WxFacePayResult, WxFacePayParam> {



    public WxFacePay(WxPayService wxPayService) {
        super(wxPayService);
    }

    /**
     * 支持类型
     *
     * @param type 类型
     * @return boolean
     */
    @Override
    boolean supportsType(PayType type) {
        return PayType.FACE_SCAN.equals(type);
    }

    /**
     * 支付
     *
     * @param payParam 支付参数
     * @return {@link WxFacePayParam}
     */
    @Override
    public WxFacePayResult pay(WxFacePayParam payParam) {
        WxPayFacepayRequest request = new WxPayFacepayRequest();
        request.setDeviceInfo(payParam.getDeviceInfo());
        request.setBody(payParam.getBody());
        request.setDetail(payParam.getDetail());
        request.setAttach(payParam.getAttach());
        request.setOutTradeNo(payParam.getOutTradeNo());
        request.setTotalFee(payParam.getTotalFee());
        request.setFeeType(payParam.getFeeType());
        request.setSpbillCreateIp(payParam.getSpbillCreateIp());
        request.setGoodsTag(payParam.getGoodsTag());
        request.setOpenid(payParam.getOpenid());
        request.setFaceCode(payParam.getFaceCode());
        request.setWorkWxSign(payParam.getWorkWxSign());
        request.setAppid(payParam.getAppId());
        request.setMchId(payParam.getMchId());
        request.setNonceStr(payParam.getNonceStr());
        request.setSubAppId(payParam.getSubAppId());
        request.setSubMchId(payParam.getSubMchId());
        request.setSign(payParam.getSign());
        request.setSignType(payParam.getSignType());

        try {

            WxPayFacepayResult result = wxPayService.facepay(request);

            WxFacePayResult payResult = new WxFacePayResult();
            payResult.setDeviceInfo(result.getDeviceInfo());
            payResult.setOpenid(result.getOpenid());
            payResult.setIsSubscribe(result.getIsSubscribe());
            payResult.setSubOpenid(result.getSubOpenid());
            payResult.setSubsSubscribe(result.getSubsSubscribe());
            payResult.setTradeType(result.getTradeType());
            payResult.setBankType(result.getBankType());
            payResult.setFeeType(result.getFeeType());
            payResult.setTotalFee(result.getTotalFee());
            payResult.setCashFeeType(result.getCashFeeType());
            payResult.setCashFee(result.getCashFee());
            payResult.setTransactionId(result.getTransactionId());
            payResult.setOutTradeNo(result.getOutTradeNo());
            payResult.setDetail(result.getDetail());
            payResult.setAttach(result.getAttach());
            payResult.setPromotionDetail(result.getPromotionDetail());
            payResult.setTimeEnd(result.getTimeEnd());
            payResult.setReturnCode(result.getReturnCode());
            payResult.setReturnMsg(result.getReturnMsg());
            payResult.setResultCode(result.getResultCode());
            payResult.setErrcode(result.getErrCode());
            payResult.setErrmsg(result.getErrCodeDes());
            payResult.setAppId(result.getAppid());
            payResult.setMchId(result.getMchId());
            payResult.setSubAppId(result.getSubAppId());
            payResult.setSubMchId(result.getSubMchId());
            payResult.setNonceStr(result.getNonceStr());
            payResult.setSign(result.getSign());

            return payResult;
        } catch (WxPayException e) {
            WxFacePayResult result = new WxFacePayResult();
            result.setErrcode(e.getErrCode());
            result.setErrmsg(e.getErrCodeDes());
            return result;
        }
    }
}
