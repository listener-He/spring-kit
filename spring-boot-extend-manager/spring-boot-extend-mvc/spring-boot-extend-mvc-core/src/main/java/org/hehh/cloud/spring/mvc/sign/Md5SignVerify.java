package org.hehh.cloud.spring.mvc.sign;

import org.hehh.cloud.spring.tool.SignUtil;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

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
