package org.hehh.cloud.spring.mvc;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-06 01:42
 * @description: 客户端设备
 */
@Data
public class UserAgent implements java.io.Serializable {

    /**
     *  名称
     */
    private String name;


    /**
     * 是否为手机
     */
    private boolean mobile;

    /**
     * 手机系统 ios 安卓
     */
    private String mobileSystem;

    /** 版本 */
    private String version;
}
