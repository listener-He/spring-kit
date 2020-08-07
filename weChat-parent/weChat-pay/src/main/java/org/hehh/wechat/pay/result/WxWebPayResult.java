package org.hehh.wechat.pay.result;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 17:08
 * @description: 微信网页支付返回
 */
@Data
public class WxWebPayResult extends WxPayResult {

    /**
     *  网页链接
     */
    private String mwebUrl;
}
