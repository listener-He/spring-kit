package org.hehh.cloud.spring.decrypt;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.security.PrivateKey;

/**
 * @author: HeHui
 * @create: 2020-03-22 00:35
 * @description: rsa 解密
 **/
public class RsaDecrypt implements IDecrypt {

    /**
     * aes解密
     */
    private final RSA rsa;


    private final KeyType keyType;


    public RsaDecrypt(RSA rsa) {


        PrivateKey key = rsa.getPrivateKey();
        if (key != null) {
            keyType = KeyType.PrivateKey;
        } else {
            keyType = KeyType.PublicKey;
        }


        this.rsa = rsa;
    }


    /**
     * 构造器
     *
     * @param privateKey 私钥
     * @param publicKey  公钥
     */
    public RsaDecrypt(String privateKey, String publicKey) {

        this(new RSA(privateKey, publicKey));
    }


    /**
     * 解密
     *
     * @param data 被解密的数据
     * @return
     */
    @Override
    public byte[] decrypt(byte[] data) {
        return rsa.decrypt(data, keyType);
    }


}
