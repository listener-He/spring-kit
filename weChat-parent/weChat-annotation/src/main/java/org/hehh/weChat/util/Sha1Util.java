package org.hehh.weChat.util;

import org.hehh.utils.StrKit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: HeHui
 * @date: 2020-08-07 10:40
 * @description: sh1加密
 */
public class Sha1Util {

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    private static char[] encodeHex(byte[] data, char[] toDigits) {
        final int len = data.length;
        final char[] out = new char[len << 1];//len*2
        // two characters from the hex value.
        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];// 高位
            out[j++] = toDigits[0x0F & data[i]];// 低位
        }
        return out;
    }



    /**
     * 加密为十六进制
     *
     * @param str str
     * @return {@link String}
     */
    public static String digestHex(String str){
        return new String(encodeHex(digest.digest(StrKit.hex2Byte(str)),DIGITS_LOWER));
    }



}

