package org.hehh.cloud.spring.decrypt;

import cn.hutool.crypto.symmetric.AES;

/**
 * @author: HeHui
 * @create: 2020-03-22 00:35
 * @description: aes加密
 **/
public class AesDecrypt implements IDecrypt {

    /**
     *  aes解密
     */
    private final AES aes;

    public AesDecrypt(AES aes){
        this.aes = aes;
    }


    public AesDecrypt(String key){
        this(new AES(key.trim().getBytes()));
    }




    /**
     * 解密
     *
     * @param data 被解密的数据
     * @return
     */
    @Override
    public byte[] decrypt(byte[] data) {
        return aes.decrypt(data);
    }
}
