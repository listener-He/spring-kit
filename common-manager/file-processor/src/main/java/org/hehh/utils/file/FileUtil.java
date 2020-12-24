package org.hehh.utils.file;


import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * @author: HeHui
 * @date: 2020-08-22 15:49
 * @description: 文件工具
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {


    /**
     * 使用AES对文件进行加密和解密
     */
    private static String AES_TYPE = "AES";


    /**
     * 写入文件 注意此方法不会关闭 InputStream 流
     *
     * @param target      目标
     * @param inputStream 输入流
     */
    public static void write(String target, InputStream inputStream) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int len;
            while (-1 != (len = inputStream.read(buf))) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 分块写入文件
     *
     * @param target          目标
     * @param targetSize      目标的大小
     * @param inputStream     输入流
     * @param inputStreamSize 输入流大小
     * @param chunks          块
     * @param chunk           块
     * @throws IOException ioexception
     */
    public static void writeWithBlock(String target, long targetSize, InputStream inputStream, long inputStreamSize, int chunks, int chunk) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(target, "rw");
            randomAccessFile.setLength(targetSize);
            if (chunk == chunks - 1) {
                randomAccessFile.seek(targetSize - inputStreamSize);
            } else {
                randomAccessFile.seek(chunk * inputStreamSize);
            }
            byte[] buf = new byte[1024];
            int len;
            while (-1 != (len = inputStream.read(buf))) {
                randomAccessFile.write(buf, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    /**
     * 获取md5摘要
     *
     * @param inputStream
     * @return
     */
    public static String md5(InputStream inputStream) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(md5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    /**
     * 获取md5摘要
     *
     * @param file
     * @return
     */
    public static String md5(File file) {
        try {
            return md5(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


//    /**
//     * 加密文件流
//     *
//     * @param input      输入
//     * @param output     输出
//     * @param privateKey 私钥
//     */
//    public static void encrypt(InputStream input, OutputStream output, String privateKey) {
//        try {
//            Key key = getKey(privateKey);
//            Cipher cipher = Cipher.getInstance(AES_TYPE + "/ECB/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            crypt(input, output, cipher);
//        } catch (Exception e) {
//        } finally {
//            try {
//                input.close();
//                output.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

//    /**
//     * 解密
//     *
//     * @param output      输出
//     * @param privateKey  私钥
//     * @param encryptFile 加密文件
//     */
//    @Deprecated
//    public static void decrypt(String encryptFile, OutputStream output, String privateKey) {
//        try {
//            decrypt(new FileInputStream(encryptFile), output, privateKey);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 解密
//     *
//     * @param input      输入
//     * @param output     输出
//     * @param privateKey 私钥
//     */
//    @Deprecated
//    public static void decrypt(InputStream input, OutputStream output, String privateKey) {
//        try {
//            Key key = getKey(privateKey);
//            Cipher cipher = Cipher.getInstance(AES_TYPE + "/ECB/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, key);
//            crypt(input, output, cipher);
//        } catch (Exception e) {
//        } finally {
//            try {
//                input.close();
//                output.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 生成指定字符串的密钥
     *
     * @param secret 要生成密钥的字符串
     * @return secretKey    生成后的密钥
     * @throws GeneralSecurityException
     */
    private static Key getKey(String secret) throws GeneralSecurityException {
        KeyGenerator kgen = KeyGenerator.getInstance(AES_TYPE);
        kgen.init(128, new SecureRandom(secret.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        return secretKey;
    }

    /**
     * 加密解密流
     *
     * @param in     加密解密前的流
     * @param out    加密解密后的流
     * @param cipher 加密解密
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static void crypt(InputStream in, OutputStream out, Cipher cipher) throws IOException, GeneralSecurityException {
        int blockSize = cipher.getBlockSize() * 1000;
        int outputSize = cipher.getOutputSize(blockSize);

        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        boolean more = true;
        while (more) {
            inLength = in.read(inBytes);
            if (inLength == blockSize) {
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                out.write(outBytes, 0, outLength);
            } else {
                more = false;
            }
        }
        if (inLength > 0)
            outBytes = cipher.doFinal(inBytes, 0, inLength);
        else
            outBytes = cipher.doFinal();
        out.write(outBytes);
    }


}

