package org.hehh.cloud.spring.decrypt.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hehh.cloud.spring.decrypt.DecryptMethod;
import org.hehh.cloud.spring.decrypt.annotation.Decrypt;

/**
 * @author: HeHui
 * @create: 2020-03-21 13:25
 * @description: 解密参数配置
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecryptParameter {


    /**
     *  解密方式
     */
    private DecryptMethod method;


    /**
     *  是否值解密模式
     *      true：只解密key对应的值，客户端请求还需正常的json格式，只是对value进行加密
     *      false: 客户端请求参数是一个被加密后对json格式，服务端对字符串进行解密
     */
    private boolean valueModel =  false;

    /**
     * 是否需要扫描注解才解密
     */
    private  boolean scanAnnotation;

    /**
     *  私钥
     */
    private String privateKey;

    /**
     *  公钥
     */
    private String publicKey;


    /**
     *  扫描的注解
     */
    private Class<? extends Decrypt> annotation = Decrypt.class;
}
