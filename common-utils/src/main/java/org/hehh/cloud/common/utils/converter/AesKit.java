package org.hehh.cloud.common.utils.converter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * @author: HeHui
 * @create: 2020-03-15 21:25
 * @description: aes加密工具
 **/
public class AesKit {



    /**
     *   生成指定长度的aes密钥
     * @param length 长度
     * @return
     * @throws Exception
     */
    public static byte[] generateDesKey(int length) throws Exception {
        //实例化
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //设置密钥长度
        kgen.init(length);
        //生成密钥
        SecretKey skey = kgen.generateKey();
        //返回密钥的二进制编码
        return  skey.getEncoded();
    }






}
