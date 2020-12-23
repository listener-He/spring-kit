package org.hehh.utils.file;

import cn.hutool.core.util.StrUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.hehh.utils.file.pojo.InputStreamFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
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
     *
     * @return boolean
     */
    public static boolean compress(OutputStream output, InputStreamFile... inputs) {
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
     * 压缩aes加密
     *
     * @param output   输出
     * @param password 密码
     * @param inputs   输入
     *
     * @return boolean
     */
    public static boolean compressEncryption(OutputStream output, String password, InputStreamFile... inputs) {
        assert output != null;
        assert StrUtil.isNotBlank(password);

        net.lingala.zip4j.io.outputstream.ZipOutputStream out = null;
        try {
             out = new net.lingala.zip4j.io.outputstream.ZipOutputStream(output, password.toCharArray());
            byte[] buffer = new byte[1024];

            for (int i = 0; i < inputs.length; i++) {
                try {

                    /**
                     *  添加文件元素开始
                     */
                    ZipParameters entry = new ZipParameters();
                    /**
                     * 设置压缩方法
                     */
                    entry.setCompressionMethod(CompressionMethod.DEFLATE);
                    /**
                     * 设置压缩级别
                     */
                    entry.setCompressionLevel(CompressionLevel.NORMAL);
                    /**
                     * 设置压缩文件是否加密
                     */
                    entry.setEncryptFiles(true);
                    /**
                     * 设置aes加密强度
                     */
                    entry.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
                    /**
                     * 设置加密方法 这里的配置要注意，配置不对将在LINUX下无法解压 AES -->  ZIP_STANDARD
                     */
                    entry.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
                    entry.setFileNameInZip(inputs[i].getName());
                    out.putNextEntry(entry);

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
}
