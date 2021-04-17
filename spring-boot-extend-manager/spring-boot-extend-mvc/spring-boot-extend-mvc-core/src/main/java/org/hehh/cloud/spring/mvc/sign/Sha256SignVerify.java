package org.hehh.cloud.spring.mvc.sign;

import org.hehh.cloud.spring.tool.SignUtil;
import org.hehh.utils.StrKit;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * @author: HeHui
 * @date: 2020-09-14 21:16
 * @description: HmAcsSha256加密签名
 */
public class Sha256SignVerify extends AbstractSignVerify {

    public static final String HMAC_SHA_256 ="HmacSHA256";


    /**
     * 信号参数排序
     *
     * @param ignore 忽略
     */
    public Sha256SignVerify(String... ignore) {
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
        try {
            StringBuilder key = new StringBuilder(param);

            Mac HMAC_SHA256 = Mac.getInstance(HMAC_SHA_256);

            if(StringUtils.hasText(secretKey)){
                key.append(secretKey);
            }

            SecretKeySpec secret = new SecretKeySpec((StringUtils.hasText(secretKey) ? (secretKey+timestamp) : timestamp+"").getBytes(SignUtil.INPUT_CHARSET),HMAC_SHA_256);
            HMAC_SHA256.init(secret);


            byte[] bytes = HMAC_SHA256.doFinal(key.toString().getBytes(SignUtil.INPUT_CHARSET));

            if ( bytes== null || bytes.length < 1){
                return null;
            }

            /**
             * 字节转换为16进制字符串
             */
            return StrKit.hexStr(bytes);

        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("找不到HmAcsSha256加密类",e);
        } catch (InvalidKeyException e) {
            throw new SignatureException("初始密钥异常",e);
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("字符串Encoding失败",e);
        }
    }
}
