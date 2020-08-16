package org.hehh.sms;

import org.hehh.cloud.common.bean.result.Result;
import org.hehh.sms.bean.Mobile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-16 23:39
 * @description: 短信发送
 */
public interface SMSSend {

    /**
     * 发送
     *
     * @param template  模板
     * @param signature 签名
     * @param mobiles   手机
     * @return {@link Result<String>}
     */
    default Result<String> send(String template,String signature,Mobile... mobiles){
        return this.send(template,signature,null,mobiles);
    }

    /**
     * 发送
     *
     * @param template  模板
     * @param signature 签名
     * @param params    参数
     * @param mobiles   手机
     * @return {@link Result<String>}
     */
    default Result<String> send(String template, String signature, Map<String,Object> params, Mobile... mobiles){
        return this.send(template,signature,params, Arrays.asList(mobiles));
    }


    /**
     * 发送
     *
     * @param template  模板
     * @param signature 签名
     * @param mobiles   手机
     * @return {@link Result<String>}
     */
    default Result<String> send(String template,String signature,Collection<Mobile> mobiles){
        return this.send(template,signature,null,mobiles);
    }


    /**
     * 发送
     *
     * @param template  模板
     * @param signature 签名
     * @param params    参数
     * @param mobiles   手机
     * @return {@link Result<String>}
     */
    Result<String> send(String template, String signature, Map<String,Object> params, Collection<Mobile> mobiles);
}
