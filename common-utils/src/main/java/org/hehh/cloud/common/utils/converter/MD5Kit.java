//package org.hehh.cloud.common.utils.converter;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.crypto.SecureUtil;
//
//import java.io.File;
//import java.io.InputStream;
//
//
///**
// * @author: HeHui
// * @create: 2020-03-20 21:58
// * @description: md5工具类, 没做什么操作。基于 SecureUtil包装一下
// **/
//public class MD5Kit {
//
//
//    /**
//     * 字符串md5加密
//     *
//     * @param str 需要加密的字符串
//     * @return
//     */
//    public static String encrypt(String str) {
//        if (StrUtil.isBlank(str)) {
//            return str;
//        }
//
//        return SecureUtil.hmacMd5().digestHex(str);
//    }
//
//
//    /**
//     * 字符串md5加密
//     *
//     * @param str  需要加密的字符串
//     * @param hmac 盐
//     * @return
//     */
//    public static String encrypt(String str, String hmac) {
//        if (StrUtil.isBlank(str)) {
//            return str;
//        }
//
//        return SecureUtil.hmacMd5(hmac).digestHex(str);
//    }
//
//
//    /**
//     * 文件md5加密
//     *
//     * @param file 需要加密的文件
//     * @return
//     */
//    public static String encrypt(File file) {
//        if (file == null) {
//            return null;
//        }
//
//        return SecureUtil.hmacMd5().digestHex(file);
//    }
//
//
//    /**
//     * 文件md5加密
//     *
//     * @param file 需要加密的文件
//     * @param hmac 盐
//     * @return
//     */
//    public static String encrypt(File file, String hmac) {
//        if (file == null) {
//            return null;
//        }
//
//        return SecureUtil.hmacMd5(hmac).digestHex(file);
//    }
//
//
//    /**
//     * 数据流md5加密
//     *
//     * @param data 需要加密的文件
//     * @return
//     */
//    public static String encrypt(InputStream data) {
//        if (data == null) {
//            return null;
//        }
//
//        return SecureUtil.hmacMd5().digestHex(data);
//    }
//
//
//
//
//    /**
//     * 数据流md5加密
//     *
//     * @param data 需要加密的文件
//     * @param hmac 盐
//     * @return
//     */
//    public static String encrypt(InputStream data, String hmac) {
//        if (data == null) {
//            return null;
//        }
//
//        return SecureUtil.hmacMd5(hmac).digestHex(data);
//    }
//
//
//}
