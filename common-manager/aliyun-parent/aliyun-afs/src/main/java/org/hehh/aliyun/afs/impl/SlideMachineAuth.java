package org.hehh.aliyun.afs.impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import org.hehh.aliyun.afs.IMachineAuth;
import org.hehh.cloud.common.bean.result.ErrorResult;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.common.bean.result.SuccessResult;

/**
 * @author: HeHui
 * @date: 2020-03-25 16:46
 * @description: 滑动人机验证
 */
public class SlideMachineAuth implements IMachineAuth {


    private final IAcsClient client;
    private final String appKey;


    public SlideMachineAuth(IAcsClient acsClient, String appKey){
        assert acsClient != null : "阿里云请求类不能为空";
        this.client = acsClient;
        this.appKey = appKey;
    }




    /**
     * 认证
     *
     * @param sessionId 会话ID（前端传）
     * @param sign      签名（前端传）
     * @param token     token （前端传）
     * @param scene     场景啊（前端传）
     * @param ip        客户端IP
     * @return
     */
    @Override
    public Result auth(String sessionId, String sign, String token, String scene, String ip) {
        AuthenticateSigRequest request = new AuthenticateSigRequest();

        /**会话ID。必填参数，从前端获取，不可更改。*/
        request.setSessionId(sessionId);
        /**签名串。必填参数，从前端获取，不可更改。*/
        request.setSig(sign);
        /**请求唯一标识。必填参数，从前端获取，不可更改。*/
        request.setToken(token);
        /**场景标识。必填参数，从前端获取，不可更改。*/
        request.setScene(scene);

        /**应用类型标识。必填参数，后端填写。*/
        request.setAppKey(appKey);
        /**
         * 客户端IP。必填参数，后端填写。
         */
        request.setRemoteIp(ip);
        try {
            /**
             * response的code枚举：100验签通过，900验签失败
             */
            AuthenticateSigResponse response = client.getAcsResponse(request);
            if(response.getCode() == 100){
                return SuccessResult.succeed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorResult.error("验证失败");
    }
}
