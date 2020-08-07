package org.hehh.wechat.pay;

/**
 * @author: HeHui
 * @date: 2020-08-07 15:30
 * @description: 支付类型
 */
public enum PayType {

    /**
     *  二维码
     */
    PAY_CODE,

    /**
     *  授权网页
     */
    JSAPI,

    /**
     *  扫一扫
     */
    NATIVE,

    /**
     *  app程序
     */
    APP,

    /**
     *  网页
     */
    H5,

    /**
     *  小程序
     */
    PROGRAM,

    /**
     *  刷脸
     */
    FACE_SCAN

}
