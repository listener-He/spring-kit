package org.hehh.wechat.pay.result;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 17:08
 * @description: 微信app支付返回
 */
@Data
public class WxJSAPIPayResult extends WxUnifiedorderPayResult {

    private String signType;

    private String paySign;
}
