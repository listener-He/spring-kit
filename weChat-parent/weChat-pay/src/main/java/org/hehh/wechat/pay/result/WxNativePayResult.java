package org.hehh.wechat.pay.result;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 17:08
 * @description: 微信扫码支付返回
 */
@Data
public class WxNativePayResult extends WxPayResult {

    /**
     *  二维码链接
     */
    private String codeUrl;
}
