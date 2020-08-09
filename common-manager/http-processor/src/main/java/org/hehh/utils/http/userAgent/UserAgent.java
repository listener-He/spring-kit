package org.hehh.utils.http.userAgent;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-06 01:42
 * @description: 客户端设备
 */
@Data
public class UserAgent implements java.io.Serializable {

    /**
     *  系统名称
     */
    private String osName;

    /**
     * 浏览器的名字
     */
    private String browserName;


    /**
     * 是否为手机
     */
    private boolean mobile;

    /**
     * 手机系统 ios 安卓
     */
    private String mobileSystem;


}
