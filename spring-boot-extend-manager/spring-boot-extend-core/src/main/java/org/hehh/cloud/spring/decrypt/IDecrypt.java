package org.hehh.cloud.spring.decrypt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author: HeHui
 * @create: 2020-03-21 13:34
 * @description: 解密接口
 **/
public interface IDecrypt {



    /**
     * 解密
     *
     * @param data 被解密的数据
     * @return
     */
    byte[] decrypt(byte[] data);






    /**
     * 解密Hex（16进制）或Base64表示的字符串
     *
     * @param data 被解密的字符串
     * @return
     */
    default byte[] decrypt(String data){
        return decrypt(Validator.isHex(data) ? HexUtil.decodeHex(data) : Base64.decode(data));
    }





    /**
     * 解密，不会关闭流
     *
     * @param data 被解密的数据刘
     * @return
     */
    default byte[] decrypt(InputStream data){
        return decrypt(IoUtil.read(data,StandardCharsets.UTF_8));
    }




    /**
     * 解密为字符串 默认编码 UTF-8
     *
     * @param data 被解密的数据
     * @return
     */
    default String decryptStr(byte[] data){
       return decryptStr(data, StandardCharsets.UTF_8);
    }




    /**
     * 解密为字符串 指定编码
     *
     * @param data    被解密的数据
     * @param charset 编码
     * @return
     */
    default String decryptStr(byte[] data, Charset charset){
        return StrUtil.str(decrypt(data), charset);
    }




    /**
     * 解密Hex为字符串 （16进制）或Base64表示的字符串，默认 utf-8
     *
     * @param data 被解密的数据
     * @return
     */
    default String decryptStr(String data){
        return decryptStr(data, StandardCharsets.UTF_8);
    }


    /**
     * 解密Hex为字符串 （16进制）或Base64表示的字符串
     *
     * @param data    被解密的字符串
     * @param charset 编码
     * @return
     */
    default String decryptStr(String data, Charset charset){
       return StrUtil.str(decrypt(data), charset);
    }


    /**
     * 解密Hex为字符串
     *
     * @param data    被解密的数据流
     * @return
     */
    default String decryptStr(InputStream data){
        return decryptStr(data,StandardCharsets.UTF_8);
    }


    /**
     * 解密Hex为字符串
     *
     * @param data    被解密的数据流
     * @param charset 编码
     * @return
     */
    default String decryptStr(InputStream data, Charset charset){
        return StrUtil.str(decrypt(data), charset);
    }

}
