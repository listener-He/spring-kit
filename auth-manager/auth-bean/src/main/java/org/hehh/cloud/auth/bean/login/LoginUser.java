package org.hehh.cloud.auth.bean.login;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


    /**
     *  是否相等
     *
     * @param obj obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if(this == obj){
            return true;
        }

        if(obj == null){
            return false;
        }

        if(obj instanceof LoginUser){
            LoginUser other = (LoginUser) obj;
            if((other.getToken() != null && !other.getToken().isEmpty())
                && (this.getToken() != null && !this.getToken().isEmpty())
                && this.getToken().equals(other.getToken())){
                return true;
            }
        }

        return false;
    }

    /**
     * 散列码
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
