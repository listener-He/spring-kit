package org.hehh.aliyun.afs;


import org.hehh.cloud.common.bean.result.Result;

/**
 * @author: HeHui
 * @date: 2020-03-25 15:59
 * @description: 人机认证接口
 */
public interface IMachineAuth {


    /**
     *  认证
     * @param sessionId 会话ID（前端传）
     * @param sign 签名（前端传）
     * @param token token （前端传）
     * @param scene 场景啊（前端传）
     * @param ip 客户端IP
     * @return
     */
    Result auth(String sessionId, String sign, String token, String scene, String ip);
}
