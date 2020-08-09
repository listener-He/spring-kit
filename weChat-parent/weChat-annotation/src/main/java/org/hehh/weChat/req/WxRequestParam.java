package org.hehh.weChat.req;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:48
 * @description: 请求参数
 */
@Data
public class WxRequestParam implements java.io.Serializable {

    /**
     * <pre>
     * 字段名：公众账号ID.
     * 变量名：appid
     * 是否必填：是
     * 类型：String(32)
     * 示例值：wxd678efh567hg6787
     * 描述：微信分配的公众账号ID（企业号corpid即为此appId）
     * </pre>
     */
    protected String appId;
}
