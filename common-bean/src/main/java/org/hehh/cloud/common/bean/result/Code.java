package org.hehh.cloud.common.bean.result;


/**
 * @author: HeHui
 * @create: 2020-03-08 17:32
 * @description: 状态码
 **/
public enum Code {


    OK(200),
    VERIFY_ERROR(4001), // 验证异常
    HTTP_4XX_ERROR(4000), // 无法解析的http请求
    SIGN_ERROR(4002),// 签名错误
    USER_SIGN_ERROR(4003),// 签名认证错误
    REST_ERROR(4004),//资源异常
    REST_METHOD_ERROR(4005),//用户请求API类型非法

    PARAM_ERROR(4020), //参数异常
    LIMITING_ERROR(4029), //限流异常
    BLACK_ERROR(4028), //黑名单
    PARAM_DECRYPT_ERROR(4121),// 参数解密异常
    PARAM_DECRYPT_KEY_ERROR(4122),// 参数解密密钥异常
    DOWNLOAD_ERROR(4220),//下载错误
    AUTH_ERROR(4300),// 权限认证错误

    FATAL_ERROR(5000), //系统异常
    LINK_ERROR(5001), //系统连接异常
    DISTRIBUTE_LOCK_ERROR(5002), //分布式锁异常
    SERVICE_DEMOTION(5003), //服务降级
    DATA(5010), // 数据库异常

    GET_TOKEN_ERROR(5101), //token获取异常




    SERVICE_ERROR(5300),//业务异常
    SERVICE_MAX_ERROR(5301);// 业务异常-超发，超最大值等等




    private final int code;


    Code(int code) {
        this.code = code;
    }



    public int getCode(){
        return this.code;
    }


    /**
     * 得到code
     *
     * @param code
     * @return
     */
    public static Code get(int code) {
        Code[] values = values();
        for (Code value : values) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

}
