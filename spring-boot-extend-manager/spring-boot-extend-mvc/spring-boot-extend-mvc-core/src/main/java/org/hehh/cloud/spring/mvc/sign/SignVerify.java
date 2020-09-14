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
     *  签名
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param paths     路径
     * @return {@link String}
     */
    default String signFrom(String body, Map<String, String[]> params,long timestamp, String... paths) throws SignatureException {
        return this.signFrom(body,params,timestamp,null,paths);
    }


    /**
     *  签名
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param paths     路径
     * @return {@link String}
     */
    default String signFrom(String body, Map<String, String[]> params,long timestamp,String secretKey, String... paths) throws SignatureException {
        return this.sign(body,SignUtil.toVerifyMap(params,false),timestamp,secretKey,paths);
    }

    /**
     *  签名
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param paths     路径
     * @return {@link String}
     */
    default String sign(String body, Map<String, String> params,long timestamp, String... paths) throws SignatureException {
        return this.sign(body,params,timestamp,null,paths);
    }


    /**
     *  签名
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param paths     路径
     * @return {@link String}
     */
    String sign(String body, Map<String, String> params,long timestamp,String secretKey, String... paths) throws SignatureException;

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
    default String sign(Map<String,String> param,String secretKey,long timestamp) throws SignatureException{
       return this.sign(null,param,timestamp,secretKey,null);
    }


    /**
     * 标志
     * 签名
     *
     * @param body     参数
     * @param timestamp 时间戳
     * @return {@link String}
     */
    default String sign(String body,long timestamp) throws SignatureException{
        return this.sign(body,null,timestamp);
    }



    /**
     * 标志
     * 签名
     *
     * @param body body参数
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param timestamp 时间戳
     * @return {@link String}
     */
    default String sign(String body,String secretKey,long timestamp) throws SignatureException{
        return this.sign(body,null,timestamp,secretKey,null);
    }

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
        return this.verify(null,param,timestamp,secretKey,null);
    }


    /**
     * 验证
     *
     * @param body     body参数
     * @param timestamp 时间戳
     * @param sign      标志
     * @return boolean
     */
    default boolean verify(String body,long timestamp,String sign) throws SignatureException{
        return this.verify(body,null,timestamp,sign);
    }



    /**
     * 验证
     *
     * @param body     body参数
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param timestamp 时间戳
     * @param sign      标志
     * @return boolean
     */
    default boolean verify(String body,String secretKey,long timestamp,String sign) throws SignatureException {
        return this.verify(body,null,timestamp,secretKey,null);
    }


    /**
     *  验签
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param paths     路径
     * @return {@link String}
     */
    default boolean verifyFrom(String body, Map<String, String[]> params,long timestamp,String sign, String... paths) throws SignatureException {
        return this.verifyFrom(body,params,timestamp,null,sign,paths);
    }


    /**
     *  验签
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param paths     路径
     * @return {@link String}
     */
    default boolean verifyFrom(String body, Map<String, String[]> params,long timestamp,String secretKey,String sign, String... paths) throws SignatureException {
        return this.verify(body,SignUtil.toVerifyMap(params,false),timestamp,secretKey,sign,paths);
    }

    /**
     *  验签
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param paths     路径
     * @return {@link String}
     */
    default boolean verify(String body, Map<String, String> params,long timestamp,String sign, String... paths) throws SignatureException {
        return this.verify(body,params,timestamp,null,sign,paths);
    }


    /**
     *  验签
     *
     * @param body      body参数
     * @param params    参数个数
     * @param timestamp 时间戳
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param paths     路径
     * @return {@link String}
     */
    default boolean verify(String body, Map<String, String> params,long timestamp,String secretKey,String sign, String... paths) throws SignatureException {
        Assert.hasText(sign,"Sign value not empty!");
        String digest = sign(body,params, timestamp, secretKey,paths);
        return StringUtils.hasText(digest) && digest.equals(sign);
    }

}
