package org.hehh.wechat.pay.result;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-07 17:18
 * @description: 微信统一支付返回
 */
@Data
public class WxUnifiedorderPayResult extends WxPayResult {

    /**
     * 由于package为java保留关键字，因此改为packageValue. 前端使用时记得要更改为package
     */
    private String packageValue;

    private String timeStamp;
}
