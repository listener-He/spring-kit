package org.hehh.aliyun.user;

import com.alipay.api.response.AlipayUserInfoShareResponse;

/**
 * @author: HeHui
 * @create: 2019-10-17 16:28
 * @description: 阿里云用户接口
 **/
public interface IAliyunUser {


    /**
     *  用户授权
     * @param auth_token
     * @return
     */
    AlipayUserInfoShareResponse authorization(String auth_token);


}
