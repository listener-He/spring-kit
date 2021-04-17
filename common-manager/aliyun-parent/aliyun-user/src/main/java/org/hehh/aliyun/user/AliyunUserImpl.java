package org.hehh.aliyun.user;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import org.hehh.aliyun.AliyunSignatureGenerate;

/**
 * @author: HeHui
 * @create: 2019-10-17 16:30
 * @description: 阿里云用户实现
 **/
public class AliyunUserImpl implements IAliyunUser {

    /**
     * 用户授权url
     */
    private static final String authorization_url = "https://openapi.alipay.com/gateway.do";


    private AliyunSignatureGenerate aliyunSignatureGenerate;

    /**
     * 阿里巴巴用户实现
     *
     * @param aliyunSignatureGenerate 王坚签名生成
     */
    public AliyunUserImpl(AliyunSignatureGenerate aliyunSignatureGenerate){
        assert aliyunSignatureGenerate != null : "IAlipaySignatureGenerate 不能为空";
        this.aliyunSignatureGenerate = aliyunSignatureGenerate;
    }




    /**
     *  用户授权
     * @param auth_token
     * @return
     */
    @Override
    public AlipayUserInfoShareResponse authorization(String auth_token) {

        /**
         *  获取 客户端请求器
         */
        AlipayClient alipayClient = aliyunSignatureGenerate.keyGenerate(authorization_url);

        /**
         *  构造用户查询请求
         */
        AlipayUserInfoShareRequest userInfoShareRequest = new AlipayUserInfoShareRequest();

        try {
            return alipayClient.execute(userInfoShareRequest, auth_token);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            AlipayUserInfoShareResponse response = new AlipayUserInfoShareResponse();
            response.setCode("20000");
            response.setMsg("Service Currently Unavailable");
            response.setSubMsg("系统繁忙");
            return response;
        }

    }
}
