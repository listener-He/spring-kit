package org.hehh.cloud.spring.mvc.sign;

import cn.hutool.core.util.ArrayUtil;
import org.hehh.cloud.spring.tool.SignUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-09-13 20:58
 * @description: 签名参数排序
 */
public abstract class AbstractSignVerify implements SignVerify {



    private final List<String> ignoreParams = new ArrayList<>();


    /**
     * 信号参数排序
     *
     * @param ignore 忽略
     */
    public AbstractSignVerify(String... ignore){
        if(ArrayUtil.isNotEmpty(ignore)){
            this.ignoreParams.addAll(Arrays.asList(ignore));
        }
    }

    /**
     * 过滤
     *
     * @param param 参数
     * @return {@link String}
     */
    public String filter(Map<String,String> param,boolean encode){
        Assert.notEmpty(param, "The ASCII sort Map cannot be empty");

        /**
         *  排除忽略的参数和为空的参数
         */
        Map<String, String> sortParam = param.entrySet().stream()
            .filter(v -> !ignoreParams.contains(v.getKey()) && StringUtils.hasText(v.getValue()))
            .sorted((k1, k2) -> {
                return k1.getKey().compareTo(k2.getKey());
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        StringBuffer sb = new StringBuffer();

        Iterator<String> keys = sortParam.keySet().iterator();
        while (keys.hasNext()){
            String k = keys.next();
            sb.append(k).append("=");

            String v = sortParam.get(k);
            if(encode){
                v = SignUtil.urlEncode(v,SignUtil.INPUT_CHARSET);
            }

            /**
             *  得到值
             */
            sb.append(v);

            /**
             *  如果还有参数
             */
            if(keys.hasNext()){
                sb.append("&");
            }
        }
        return sb.toString();
    }


    /**
     * 签名
     *
     * @param param     参数
     * @param secretKey 加密密钥(加密盐)，为防止被穷举。为空则不加密
     * @param timestamp 时间戳
     * @return {@link String}
     */
    @Override
    public String sign(Map<String, String> param, String secretKey, long timestamp) throws SignatureException {
        String digest = digest(filter(param,false), secretKey, timestamp) + timestamp;
        try {
            return SignUtil.encodeBase64Str(digest.getBytes(SignUtil.INPUT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("摘要encode异常",e);
        }

    }



    /**
     * 摘要
     *
     * @param param     参数
     * @param secretKey 秘密密钥
     * @param timestamp 时间戳
     * @return {@link String}
     */
    protected abstract String digest(String param, String secretKey, long timestamp) throws SignatureException;

}
