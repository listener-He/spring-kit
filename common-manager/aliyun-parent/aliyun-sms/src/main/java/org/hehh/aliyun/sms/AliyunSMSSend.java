package org.hehh.aliyun.sms;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.hehh.cloud.common.bean.result.ErrorResult;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.common.bean.result.SuccessResult;
import org.hehh.sms.SMSSend;
import org.hehh.sms.bean.Mobile;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-08-16 23:52
 * @description: 阿里大鱼短信发送
 */
public class AliyunSMSSend implements SMSSend {


    /**
     * 产品名称:云通信短信API产品
     */
    static final String product = "Dysmsapi";
    /**
     * 产品域名
     */
    static final String domain = "dysmsapi.aliyuncs.com";


    /**
     * 访问密钥id
     */
    private final String accessKeyId;

    /**
     * 访问密钥的秘密
     */
    private final String accessKeySecret;

    /**
     * 启用
     */
    private final Boolean enable;


    /**
     *
     *  构造器
     * @param accessKeyId     访问密钥id
     * @param accessKeySecret 访问密钥的秘密
     * @param enable          启用
     */
    public AliyunSMSSend(String accessKeyId, String accessKeySecret, Boolean enable) {

        assert accessKeyId != null : "短信appID不能为空!";
        assert accessKeySecret != null : "短信密钥不能为空!" ;

        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.enable = enable == null ? true : enable;
    }


    /**
     * 发送
     *
     * @param template  模板
     * @param signature 签名
     * @param params    参数
     * @param mobiles   手机
     * @return {@link Result <String>}
     */
    @Override
    public Result<String> send(String template, String signature, Map<String, Object> params, Collection<Mobile> mobiles) {

        /**
         * 测试环境不发送短信
         */
        if (!enable) {
            return SuccessResult.succeed();
        }

        if (mobiles.size() >= 1000) {
            SendSmsResponse response = requestSend(params, signature, template, mobiles.stream().map(v -> v.toString()).collect(Collectors.toSet()));
            if(response.getCode().equalsIgnoreCase("ok")){
                return SuccessResult.succeed(response.getRequestId());
            }
            return ErrorResult.error(response.getMessage());
        }

        throw new RuntimeException("单次发送不能超过1000个手机");
    }




    /**
     * 请求发送
     *
     * @param templateParam 模板参数
     * @param signature     签名
     * @param templateCode  模板代码
     * @param telephone     电话
     * @return {@link SendSmsResponse}
     */
    public SendSmsResponse requestSend(Map<String, Object> templateParam, String signature, String templateCode, Set<String> telephone) {
        /**可自助调整超时时间*/
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        try {
            /**初始化acsClient,暂不支持region化*/
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            /**组装请求对象-具体描述见控制台-文档部分内容*/
            SendSmsRequest request = new SendSmsRequest();
            /**必填:待发送手机号*/
            request.setPhoneNumbers(CollUtil.join(telephone, ","));
            /**必填:短信签名-可在短信控制台中找到*/
            request.setSignName(signature);
            /**必填:短信模板-可在短信控制台中找到*/
            request.setTemplateCode(templateCode);

            if (null != templateParam) {
                /**
                 * 可选:模板中的变量替换JSON串
                 */
                request.setTemplateParam(JSONUtil.toJsonStr(templateParam));
            }


            return acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            SendSmsResponse sendSmsResponse1 = new SendSmsResponse();
            sendSmsResponse1.setCode("1");
            return sendSmsResponse1;
        }
    }

}
