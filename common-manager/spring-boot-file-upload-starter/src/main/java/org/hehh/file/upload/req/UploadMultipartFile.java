package org.hehh.file.upload.req;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: HeHui
 * @date: 2021-01-27 15:14
 * @description: 上传单个文件请求
 */
public class UploadMultipartFile extends UploadBase {

    /**
     * 文件
     */
    private MultipartFile file;

    /**
     * 文件名 (保存路径)
     */
    private String filename;


    /**
     * 得到文件
     *
     * @return {@link MultipartFile}
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * 设置文件
     *
     * @param file 文件
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    /**
     * 获取文件名
     *
     * @return {@link String}
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 设置文件名
     *
     * @param filename 文件名
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 文件名字
     *
     * @param filename 文件名
     *
     * @return {@link UploadMultipartFile}
     */
    public UploadMultipartFile name(String filename) {
        setFilename(filename);
        return this;
    }

    /**
     * 构建
     *
     * @param file 文件
     *
     * @return {@link UploadMultipartFile}
     */
    public static UploadMultipartFile build(MultipartFile file) {
        UploadMultipartFile req = new UploadMultipartFile();
        req.setFile(file);
        return req;
    }


}
