package org.hehh.sms.bean;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-03-23 17:12
 * @description: 手机
 */
@Data
public class Mobile {


    /**
     * 代码
     */
    private String code;

    /**
     * 移动
     */
    private String mobile;


    /**
     * 设置代码
     *
     * @param code 代码
     */
    public void setCode(String code) {
        assert StrUtil.isNotBlank(code) : "code不能为空";
        this.code = code;
    }

    /**
     * 设置手机
     *
     * @param mobile 移动
     */
    public void setMobile(String mobile) {
        assert StrUtil.isNotBlank(mobile) : "mobile不能为空";
        this.mobile = mobile;
    }


    /**
     * 构建
     *
     * @param mobile 移动
     * @return {@link Mobile}
     */
    public static Mobile build(String mobile) {
        Mobile m = new Mobile();
        m.setMobile(mobile);
        return m;
    }


    /**
     * 代码
     *
     * @param code 代码
     * @return {@link Mobile}
     */
    public Mobile code(String code) {
        setCode(code);
        return this;
    }


    /**
     * toString 方法，如果是86就不需要拼接区号
     *
     * @return
     */
    @Override
    public String toString() {
        return code.trim().equals("86") ? mobile.trim() : code.trim() + mobile.trim();
    }


    /**
     * 平等的
     *
     * @param mobile 移动
     * @return boolean
     */
    public boolean equals(Mobile mobile){
        if(mobile == null){
            return false;
        }
        return mobile.toString().equals(this.toString());
    }

}
