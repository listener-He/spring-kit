package org.hehh.utils.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2020-08-22 15:30
 * @description: 上传文件存储
 */
public interface UploadFileStorage {


    default void verifyType(MultipartFile file, String type) throws IOException {
        if (file != null && !file.isEmpty()) {

        }
    }


    /**
     * 上传
     * 文件名默认hash
     *
     * @param file 文件
     *
     * @return {@link String}
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    default String upload(MultipartFile file) throws FileNotFoundException {
        return this.upload(file, null);
    }


    /**
     * 上传
     *
     * @param file     文件
     * @param filename 文件名
     *
     * @return {@link String}* @throws FileNotFoundException 文件未发现异常
     */
    default String upload(MultipartFile file, String filename) throws FileNotFoundException {
        return this.upload(file, filename, null);
    }


    /**
     * 上传指定目录
     * 文件名默认hash
     *
     * @param file      文件
     * @param directory 目录
     *
     * @return {@link String}
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    default String uploadDirectory(MultipartFile file, String directory) throws FileNotFoundException {
        return upload(file, null, directory);
    }


    /**
     * 上传
     *
     * @param file      文件
     * @param filename  文件名
     * @param directory 目录
     *
     * @return {@link String}* @throws FileNotFoundException 文件未发现异常
     */
    String upload(MultipartFile file, String filename, String directory) throws FileNotFoundException;



}
