package org.hehh.cloud.auth.bean.login;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author : HeHui
 * @date : 2019-02-26 10:35
 * @describe : 当前登录用户
 */
@Data
@NoArgsConstructor
public class LoginUser implements java.io.Serializable {


    /**
     *  用户ID
     */
    private String userId;


    /**
     *  登陆应用
     */
    private Integer appId;

    /**
     *  显示名称
     */
    private String name;

    /**
     *  当前登陆唯一ID
     */
    private String token;


    /**
     *  关联ID
     */
    private String associatedId;



    /**
     *  用户类型
     */
    private int userType;


    /**
     *  登陆设备 1pc，2手机
     */
    private int equipment;


    /**
     *  过期时间(毫秒)
     */
    private long overdueTime;


    /**
     *  角色ID
     */
    private String roleId;

    /**
     *  生成时间
     */
    private Long createTime;

}
