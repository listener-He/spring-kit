package org.hehh.cloud.spring.mvc.sign;

import org.hehh.cloud.spring.tool.SignUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.SignatureException;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-09-12 17:49
 * @description: 签名验证
 */
public interface SignVerify {


    /**
     * 标志
     * 签名
     *
     * @param param     参数
     * @param timestamp 时间戳
     * @return {@link String}
     */
    default String signFrom(Map<String,String[]> param,long timestamp) throws SignatureException{
        return this.signFrom(param,null,timestamp);
    }


    /**
     * 标志
     * 签名
     *
     * @param param     参数
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param timestamp 时间戳
     * @return {@link String}
     */
    default String signFrom(Map<String,String[]> param,String secretKey,long timestamp) throws SignatureException{
        return this.sign(SignUtil.toVerifyMap(param, true),secretKey,timestamp);
    }


    /**
     * 标志
     * 签名
     *
     * @param param     参数
     * @param timestamp 时间戳
     * @return {@link String}
     */
    default String sign(Map<String,String> param,long timestamp) throws SignatureException{
        return this.sign(param,null,timestamp);
    }


    /**
     * 标志
     * 签名
     *
     * @param param     参数
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param timestamp 时间戳
     * @return {@link String}
     */
    String sign(Map<String,String> param,String secretKey,long timestamp) throws SignatureException;


    /**
     * 验证表单
     *
     * @param param     参数
     * @param timestamp 时间戳
     * @param sign      标志
     * @return boolean
     */
    default boolean verifyForm(Map<String,String[]> param,long timestamp,String sign) throws SignatureException{
        return this.verifyForm(param,null,timestamp,sign);
    }

    /**
     * 验证表单
     *
     * @param param     参数
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param timestamp 时间戳
     * @param sign      标志
     * @return boolean
     */
    default boolean verifyForm(Map<String,String[]> param,String secretKey,long timestamp,String sign) throws SignatureException{
       return this.verify(SignUtil.toVerifyMap(param,true),secretKey,timestamp,sign);
    }



    /**
     * 验证
     *
     * @param param     参数
     * @param timestamp 时间戳
     * @param sign      标志
     * @return boolean
     */
    default boolean verify(Map<String,String> param,long timestamp,String sign) throws SignatureException{
        return this.verify(param,null,timestamp,sign);
    }



    /**
     * 验证
     *
     * @param param     参数
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param timestamp 时间戳
     * @param sign      标志
     * @return boolean
     */
    default boolean verify(Map<String,String> param,String secretKey,long timestamp,String sign) throws SignatureException {
        Assert.hasText(sign,"Sign value not empty!");
        String digest = sign(param, secretKey, timestamp);
        return StringUtils.hasText(digest) && digest.equals(sign);
    }




}
