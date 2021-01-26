package org.hehh.file.upload.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.hehh.file.upload.UploadFileStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author: HeHui
 * @date: 2020-08-22 16:52
 * @description: 七牛文件上传
 */
public interface QiNiuFileUpload extends UploadFileStorage {


    /**
     * 上传
     * 文件名默认hash
     *
     * @param inputStream 文件
     * @param bucket      桶
     *
     * @return {@link String}
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    default String upload(InputStream inputStream, Bucket bucket) throws FileNotFoundException {
        return this.upload(inputStream, null, bucket);
    }


    /**
     * 上传
     *
     * @param filename    文件名
     * @param bucket      桶
     * @param inputStream 输入流
     *
     * @return {@link String}
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    default String upload(InputStream inputStream, String filename, Bucket bucket) throws FileNotFoundException {
        return this.upload(inputStream, filename, null, bucket);
    }


    /**
     * 上传指定目录
     * 文件名默认hash
     *
     * @param directory   目录
     * @param bucket      桶
     * @param inputStream 输入流
     *
     * @return {@link String}
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    default String uploadDirectory(InputStream inputStream, String directory, Bucket bucket) throws FileNotFoundException {
        return this.upload(inputStream, null, directory, bucket);
    }


    /**
     * 上传
     *
     * @param filename    文件名
     * @param directory   目录
     * @param bucket      桶
     * @param inputStream 输入流
     *
     * @return {@link String}
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    String upload(InputStream inputStream, String filename, String directory, Bucket bucket) throws FileNotFoundException;


//    /**
//     * 上传
//     *
//     * @param file      文件
//     * @param filename  文件名
//     * @param directory 目录
//     *
//     * @return {@link String}
//     *
//     * @throws IOException 文件未发现异常
//     */
//    @Override
//    default String upload(MultipartFile file, String filename, String directory) throws IOException {
//        try {
//            return this.upload(file.getInputStream(), filename, directory, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    /**
     * 上传
     *
     * @param url url
     *
     * @return {@link String}
     */
    default String upload(String url) throws QiniuException {
        return this.upload(url, null, null);
    }

    /**
     * 上传
     *
     * @param url      url
     * @param filename 文件名
     *
     * @return {@link String}
     */
    default String upload(String url, String filename) throws QiniuException {
        return this.upload(url, filename, null);
    }

    /**
     * 上传
     *
     * @param url    url
     * @param bucket 桶
     *
     * @return {@link String}
     */
    default String upload(String url, Bucket bucket) throws QiniuException {
        return this.upload(url, null, bucket);
    }


    /**
     * 上传
     *
     * @param url      url
     * @param filename 文件名
     * @param bucket   桶
     *
     * @return {@link String}
     */
    String upload(String url, String filename, Bucket bucket) throws QiniuException;


    /**
     * 上传
     *
     * @param file 文件
     *
     * @return {@link String}
     */
    default String upload(File file) throws FileNotFoundException {
        return this.upload(file, null, null);
    }

    /**
     * 上传
     *
     * @param file     文件
     * @param filename 文件名
     *
     * @return {@link String}
     */
    default String upload(File file, String filename) throws FileNotFoundException {
        return this.upload(file, filename, null);
    }

    /**
     * 上传
     *
     * @param file   文件
     * @param bucket 桶
     *
     * @return {@link String}
     *
     * @throws QiniuException qiniu例外
     */
    default String upload(File file, Bucket bucket) throws FileNotFoundException {
        return this.upload(file, null, bucket);
    }


    /**
     * 上传
     *
     * @param filename 文件名
     * @param bucket   桶
     * @param file     文件
     *
     * @return {@link String}
     *
     * @throws QiniuException qiniu例外
     */
    String upload(File file, String filename, Bucket bucket) throws FileNotFoundException;


    /**
     * 删除
     *
     * @param url url
     *
     * @return {@link Response}
     */
    default Response delete(String url) throws QiniuException {
        return this.delete(url, null);
    }


    /**
     * 删除
     *
     * @param url    url
     * @param bucket 桶
     *
     * @return {@link Response}
     */
    Response delete(String url, Bucket bucket) throws QiniuException;


    /**
     * 删除后
     *
     * @param url  url
     * @param days 天
     *
     * @return {@link Response}
     */
    default Response deleteAfter(String url, int days) throws QiniuException {
        return this.deleteAfter(url, days, null);
    }

    /**
     * 删除后
     *
     * @param url    url
     * @param days   天
     * @param bucket 桶
     *
     * @return {@link Response}
     */
    Response deleteAfter(String url, int days, Bucket bucket) throws QiniuException;


    /**
     * 上传的令牌
     *
     * @param filename 文件名
     * @param expires  到期
     *
     * @return {@link String}
     */
    default String uploadToken(String filename, long expires) {
        return this.uploadToken(filename, expires, null);
    }


    /**
     * 上传的令牌
     *
     * @param filename 文件名
     * @param expires  到期
     * @param bucket   桶
     *
     * @return {@link String}
     */
    String uploadToken(String filename, long expires, Bucket bucket);


    /**
     * 生成私人网址
     *
     * @param url             url
     * @param expireInSeconds 在几秒钟内到期
     *
     * @return {@link String}
     */
    default String generatePrivateUrl(String url, long expireInSeconds) {
        return this.generatePrivateUrl(url, expireInSeconds, null);
    }

    /**
     * 生成私人网址
     *
     * @param url             url
     * @param expireInSeconds 在几秒钟内到期
     * @param bucket          桶
     *
     * @return {@link String}
     */
    String generatePrivateUrl(String url, long expireInSeconds, Bucket bucket);


}
