package org.hehh.file.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2020-08-22 15:30
 * @description: 上传文件存储
 */
public interface UploadFileStorage {


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
    default String upload(MultipartFile file) throws IOException {
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
    default String upload(MultipartFile file, String filename) throws IOException {
        return this.upload(file, filename, null, null);
    }

    default String upload(MultipartFile file, String filename, String... types) throws IOException {
        return upload(file, filename, null, types);
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
     * @throws IOException 文件未发现异常
     */
    default String uploadDirectory(MultipartFile file, String directory) throws IOException {
        return uploadDirectory(file, directory, null);
    }

    /**
     * 上传目录
     *
     * @param file      文件
     * @param directory 目录
     * @param types     类型
     *
     * @return {@link String}
     *
     * @throws IOException 文件未发现异常
     */
    default String uploadDirectory(MultipartFile file, String directory, String... types) throws IOException {
        return upload(file, null, directory, types);
    }


    /**
     * 上传
     *
     * @param file      文件
     * @param filename  文件名
     * @param directory 目录
     * @param types     类型
     *
     * @return {@link String}
     *
     * @throws IOException 文件未发现异常
     */
    default String upload(MultipartFile file, String filename, String directory, String... types) throws IOException {
        UploadMultipartFileReq req = UploadMultipartFileReq.build(file).name(filename);
        return upload(file,directory,types);
    }


    /**
     * 上传
     *
     * @param file      文件
     * @param filename  文件名
     * @param directory 目录
     *
     * @return {@link String}
     *
     * @throws IOException 文件未发现异常
     */
    default String upload(MultipartFile file, String filename, String directory) throws IOException {
        return upload(file, filename, directory, null);
    }


    /**
     * 上传
     *
     * @param req   请求参数
     * @param types 类型
     *
     * @return {@link String}
     */
    default String upload(UploadMultipartFileReq req, String... types) throws IOException {
        return upload(req, null, types);
    }

    /**
     * 上传 (核心方法，所有上传基于这个方法)
     *
     * @param req       请求参数
     * @param directory 目录
     * @param types     类型
     *
     * @return {@link String}
     */
    String upload(UploadMultipartFileReq req, String directory, String... types) throws IOException;


}
