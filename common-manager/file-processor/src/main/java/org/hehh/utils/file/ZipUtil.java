package org.hehh.utils.file;

import org.hehh.utils.file.pojo.InputStreamFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author: HeHui
 * @date: 2020-12-22 19:31
 * @description: zip工具类
 */
public class ZipUtil {


    /**
     * 压缩文件
     *
     * @param output 输出
     * @param inputs 输入
     * @return boolean
     */
    public static boolean zip(OutputStream output, InputStreamFile... inputs) {
        ZipOutputStream out = new ZipOutputStream(output);

        byte[] buffer = new byte[1024];
        try {
            for (int i = 0; i < inputs.length; i++) {
                try {

                    /**
                     *  添加文件元素开始
                     */
                    out.putNextEntry(new ZipEntry(inputs[i].getName()));

                    int len;
                    /**
                     * 读入需要下载的文件的内容，打包到zip文件
                     */
                    while ((len = inputs[i].getStream().read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    /**
                     *  添加文件元素结束
                     */
                    out.closeEntry();
                    /**
                     *  输入流关闭
                     */
                    inputs[i].getStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return true;
    }


    /**
     * 对压缩文件进行加密
     */
    public void encrypt(String filePath, String zipFileName, String password) {
        try {
            //设置压缩文件参数
            ZipParameters parameters = new ZipParameters();
            //设置压缩方法
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            //设置压缩级别
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            //设置压缩文件是否加密
            parameters.setEncryptFiles(true);
            //设置aes加密强度
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            //设置加密方法
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            //设置密码
            parameters.setPassword(password.toCharArray());
            //压缩文件,并生成压缩文件
            ArrayList<File> filesToAdd = new ArrayList<File>();
            File file = new File(filePath);
            filesToAdd.add(file);
            ZipFile zipFile = new ZipFile(zipFileName);
            zipFile.addFiles(filesToAdd, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }

    }
}
