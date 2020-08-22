package org.hehh.utils.file;


import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.security.MessageDigest;

/**
 * @author: HeHui
 * @date: 2020-08-22 15:49
 * @description: 文件工具
 */
public class FileUtil {


    /**
     * 写入文件 注意此方法不会关闭 InputStream 流
     *
     * @param target      目标
     * @param inputStream 输入流
     */
    public static void write(String target, InputStream inputStream)  {
        OutputStream os = null;
        try {
            os = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int len;
            while (-1 != (len = inputStream.read(buf))) {
                os.write(buf,0,len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     *  分块写入文件
     *
     * @param target          目标
     * @param targetSize      目标的大小
     * @param inputStream     输入流
     * @param inputStreamSize 输入流大小
     * @param chunks          块
     * @param chunk           块
     * @throws IOException ioexception
     */
    public static void writeWithBlock(String target, long targetSize, InputStream inputStream, long inputStreamSize, int chunks, int chunk){
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(target,"rw");
            randomAccessFile.setLength(targetSize);
            if (chunk == chunks - 1) {
                randomAccessFile.seek(targetSize - inputStreamSize);
            } else {
                randomAccessFile.seek(chunk * inputStreamSize);
            }
            byte[] buf = new byte[1024];
            int len;
            while (-1 != (len = inputStream.read(buf))) {
                randomAccessFile.write(buf,0,len);
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }





    /**
     *  获取md5摘要
     * @param inputStream
     * @return
     */
    public static String md5(InputStream inputStream){
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  获取md5摘要
     * @param file
     * @return
     */
    public static String md5(File file){
        try {
            return md5(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

