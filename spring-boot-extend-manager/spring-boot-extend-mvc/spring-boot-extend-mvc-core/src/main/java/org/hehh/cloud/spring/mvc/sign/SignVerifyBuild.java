package org.hehh.cloud.spring.mvc.sign;

/**
 * @author: HeHui
 * @date: 2020-09-15 01:11
 * @description: 签名验证构建工厂
 */
public class SignVerifyBuild {


    /**
     * 构建
     *
     * @param model  模型
     * @param ignore 忽略
     * @return {@link SignVerify}
     */
    public static SignVerify build(SignatureModel model,String... ignore){
        switch (model){
            case MD5: return new Md5SignVerify(ignore);
            case HMAC_SHA256: return new Sha256SignVerify(ignore);
            default: return null;
        }
    }
}
