package org.hehh.cloud.auth.bean.login;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private String appId;

    /**
     *  显示名称
     */
    private String name;


    /**
     *  当前登陆唯一ID
     */
    private String token;

    /**
     * 用户类型
     */
    private int userType;

    /**
     *  过期时间(毫秒)
     */
    private long overdueTime;

    /**
     *  生成时间
     */
    private Long createTime;



    public Long longUserId(){
        if(null != userId){
            return Long.valueOf(userId);
        }
        return null;
    }

    public Integer intUserId(){
        if(null != userId){
            return Integer.valueOf(userId);
        }
        return null;
    }

}
