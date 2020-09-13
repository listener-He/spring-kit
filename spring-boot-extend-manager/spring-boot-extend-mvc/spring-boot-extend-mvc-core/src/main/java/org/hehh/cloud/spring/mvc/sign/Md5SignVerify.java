package org.hehh.cloud.spring.mvc.sign;

import org.hehh.cloud.spring.tool.SignUtil;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-09-13 16:40
 * @description: md5签名验证
 */
public class Md5SignVerify extends AbstractSignVerify  {


    /**
     * mds签名验证
     *
     * @param ignore 忽略
     */
    public Md5SignVerify(String... ignore) {
        super(ignore);
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
    @Override
    public String sign(Map<String, String> param, String secretKey, long timestamp) {

        try {
            StringBuilder key = new StringBuilder(filter(param,false));
            if(StringUtils.hasText(secretKey)){
                key.append(secretKey);
            }
            String digest = DigestUtils.md5DigestAsHex(key.toString().getBytes(SignUtil.INPUT_CHARSET))+timestamp+"";
            return SignUtil.encodeBase64Str(digest.getBytes(SignUtil.INPUT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 摘要
     *
     * @param param     参数
     * @param secretKey 秘密密钥
     * @param timestamp 时间戳
     * @return {@link String}
     */
    @Override
    protected String digest(String param, String secretKey, long timestamp) throws SignatureException {
        StringBuilder key = new StringBuilder(param);
        if(StringUtils.hasText(secretKey)){
            key.append(secretKey);
        }
        try {
            return DigestUtils.md5DigestAsHex(key.toString().getBytes(SignUtil.INPUT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("Sign加密异常",e);
        }
    }
}
