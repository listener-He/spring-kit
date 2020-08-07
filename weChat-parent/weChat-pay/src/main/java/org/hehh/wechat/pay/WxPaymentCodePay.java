package org.hehh.wechat.pay;

import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.hehh.wechat.pay.req.WxPaymentCodeParam;
import org.hehh.wechat.pay.result.WxPaymentCodeResult;

/**
 * @author: HeHui
 * @date: 2020-08-07 16:02
 * @description: 付款码支付
 */
public class WxPaymentCodePay extends AbstractBinarywangWxPay<WxPaymentCodeResult, WxPaymentCodeParam> {


    /**
     * wx付款码支付
     *
     * @param wxPayService wx支付服务
     */
    public WxPaymentCodePay(WxPayService wxPayService) {
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
        return PayType.PAY_CODE.equals(type);
    }

    /**
     * 支付
     *
     * @param payParam 支付参数
     * @return {@link WxPaymentCodeParam}
     */
    @Override
    public WxPaymentCodeResult pay(WxPaymentCodeParam payParam) {

        try {
            WxPayMicropayRequest request = new WxPayMicropayRequest();
            request.setDeviceInfo(payParam.getDeviceInfo());
            request.setVersion(payParam.getVersion());
            request.setBody(payParam.getBody());
            request.setDetail(payParam.getDetail());
            request.setAttach(payParam.getAttach());
            request.setOutTradeNo(payParam.getOutTradeNo());
            request.setTotalFee(payParam.getTotalFee());
            request.setFeeType(payParam.getFeeType());
            request.setSpbillCreateIp(payParam.getSpbillCreateIp());
            request.setGoodsTag(payParam.getGoodsTag());
            request.setLimitPay(payParam.getLimitPay());
            request.setTimeStart(payParam.getTimeStart());
            request.setTimeExpire(payParam.getTimeExpire());
            request.setReceipt(payParam.getReceipt());
            request.setAuthCode(payParam.getAuthCode());
            request.setSceneInfo(payParam.getSceneInfo());
            request.setProfitSharing(payParam.getProfitSharing());
            request.setWorkWxSign(payParam.getWorkWxSign());
            request.setAppid(payParam.getAppId());
            request.setMchId(payParam.getMchId());
            request.setNonceStr(payParam.getNonceStr());
            request.setSubAppId(payParam.getSubAppId());
            request.setSubMchId(payParam.getSubMchId());
            request.setSign(payParam.getSign());
            request.setSignType(payParam.getSignType());




            WxPayMicropayResult micropay = wxPayService.micropay(request);



            WxPaymentCodeResult result = new WxPaymentCodeResult();
            result.setOpenid(micropay.getOpenid());
            result.setIsSubscribe(micropay.getIsSubscribe());
            result.setTradeType(micropay.getTradeType());
            result.setBankType(micropay.getBankType());
            result.setFeeType(micropay.getFeeType());
            result.setTotalFee(micropay.getTotalFee());
            result.setSettlementTotalFee(micropay.getSettlementTotalFee());
            result.setCouponFee(micropay.getCouponFee());
            result.setCashFeeType(micropay.getCashFeeType());
            result.setCashFee(micropay.getCashFee());
            result.setTransactionId(micropay.getTransactionId());
            result.setOutTradeNo(micropay.getOutTradeNo());
            result.setAttach(micropay.getAttach());
            result.setTimeEnd(micropay.getTimeEnd());
            result.setPromotionDetail(micropay.getPromotionDetail());
            result.setReturnCode(micropay.getReturnCode());
            result.setReturnMsg(micropay.getReturnMsg());
            result.setResultCode(micropay.getResultCode());
            result.setErrCode(micropay.getErrCode());
            result.setErrCodeDes(micropay.getErrCodeDes());
            result.setAppId(micropay.getAppid());
            result.setMchId(micropay.getMchId());
            result.setSubAppId(micropay.getSubAppId());
            result.setSubMchId(micropay.getSubMchId());
            result.setNonceStr(micropay.getNonceStr());
            result.setSign(micropay.getSign());
        return result;
        } catch (WxPayException e) {
            WxPaymentCodeResult result = new WxPaymentCodeResult();
            result.setErrCode(e.getErrCode());
            result.setErrCodeDes(e.getErrCodeDes());
            return result;
        }
    }
}
