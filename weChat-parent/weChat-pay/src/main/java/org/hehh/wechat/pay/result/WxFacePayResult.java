package org.hehh.wechat.pay.result;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 18:27
 * @description: 微信刷脸支付返回
 */
@Data
public class WxFacePayResult extends WxPayResult {

    /**
     * <pre>
     * 设备号.
     * device_info
     * 否
     * String(32)
     * 013467007045764
     * 调用接口提交的终端设备号
     * </pre>
     */
    private String deviceInfo;

    /**
     * <pre>
     * 用户标识.
     * openid
     * 是
     * String(128)
     * Y
     * 用户在商户appid 下的唯一标识
     * </pre>
     **/
    private String openid;

    /**
     * <pre>
     * 是否关注公众账号.
     * is_subscribe
     * 是
     * String(1)
     * Y
     * 用户是否关注公众账号，仅在公众账号类型支付有效，取值范围：Y或N;Y-关注;N-未关注
     * </pre>
     **/
    private String isSubscribe;

    /**
     * <pre>
     * 用户子标识.
     * sub_openid
     * 否
     * String(128)
     * Y
     * 子商户appid下用户唯一标识，如需返回则请求时需要传sub_appid
     * </pre>
     **/
    private String subOpenid;

    /**
     * <pre>
     * 是否关注子公众账号.
     * sub_is_subscribe
     * 是
     * String(1)
     * Y
     * 用户是否关注子公众账号，仅在公众账号类型支付有效，取值范围：Y或N;Y-关注;N-未关注
     * </pre>
     **/
    private String subsSubscribe;

    /**
     * <pre>
     * 交易类型.
     * trade_type
     * 是
     * String(16)
     * FACEPAY
     * 支付类型为 FACEPAY(即刷脸支付)
     * </pre>
     **/
    private String tradeType;

    /**
     * <pre>
     * 付款银行.
     * bank_type
     * 是
     * String(32)
     * CMC
     * 银行类型，采用字符串类型的银行标识，值列表详见银行类型
     * </pre>
     **/
    private String bankType;

    /**
     * <pre>
     * 货币类型.
     * fee_type
     * 否
     * String(16)
     * CNY
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     * </pre>
     **/
    private String feeType;

    /**
     * <pre>
     * 订单金额.
     * total_fee
     * 是
     * Int
     * 888
     * 订单总金额，单位为分，只能为整数，详见支付金额
     * </pre>
     **/
    private Integer totalFee;

    /**
     * <pre>
     * 现金支付货币类型.
     * cash_fee_type
     * 否
     * String(16)
     * CNY
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     * </pre>
     **/
    private String cashFeeType;

    /**
     * <pre>
     * 现金支付金额.
     * cash_fee
     * 是
     * Int
     * 100
     * 订单现金支付金额，详见支付金额
     * </pre>
     **/
    private Integer cashFee;

    /**
     * <pre>
     * 微信支付订单号.
     * transaction_id
     * 是
     * String(32)
     * 1217752501201407033233368018
     * 微信支付订单号
     * </pre>
     **/
    private String transactionId;

    /**
     * <pre>
     * 商户订单号.
     * out_trade_no
     * 是
     * String(32)
     * 1217752501201407033233368018
     * 商户系统的订单号，与请求一致。
     * </pre>
     **/
    private String outTradeNo;

    /**
     * <pre>
     * 商品详情.
     * detail
     * 否
     * String(8192)
     * 与提交数据一致
     * 实际提交的返回
     * </pre>
     **/
    private String detail;

    /**
     * <pre>
     * 商家数据包.
     * attach
     * 否
     * String(128)
     * 123456
     * 商家数据包，原样返回
     * </pre>
     **/
    private String attach;

    /**
     * <pre>
     * 营销详情.
     * promotion_detail
     * 否
     * String(6000)
     * 示例见下文
     * 新增返回，单品优惠功能字段，需要接入请见详细说明
     * </pre>
     **/
    private String promotionDetail;

    /**
     * <pre>
     * 支付完成时间.
     * time_end
     * 是
     * String(14)
     * 20141030133525
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。详见时间规则
     * </pre>
     **/
    private String timeEnd;

}
