package org.hehh.cloud.spring.decrypt;

import cn.hutool.crypto.symmetric.DES;

/**
 * @author: HeHui
 * @create: 2020-03-22 00:35
 * @description: aes解密
 **/
public class DesDecrypt implements IDecrypt {

    /**
     *  des解密
     */
    private final DES des;

    public DesDecrypt(DES des){
        this.des = des;
    }


    /**
     *
     * @param key 密钥
     */
    public DesDecrypt(String key){
        this(new DES(key.getBytes()));
    }




    /**
     * 解密
     *
     * @param data 被解密的数据
     * @return
     */
    @Override
    public byte[] decrypt(byte[] data) {
        return des.decrypt(data);
    }
}
