package org.hehh.aliyun.afs;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-03-25 17:09
 * @description: 验证参数
 */
@Data
public class AuthParam {


    /**
     *  会话
     */
    private String sessionId;

    /**
     *  签名
     */
    private String sig;

    /**
     *  token
     */
    private String token;

    /**
     *  场景
     */
    private String scene;

    /**
     *  客户端ID
     */
    private String ip;
}
