package org.hehh.file.upload.config;

import lombok.Data;


/**
 * @author: HeHui
 * @date: 2020-08-22 18:15
 * @description: 本地上传参数
 */
@Data
public class LocalUploadParameters {

    /**
     * 域
     */
    private String domain;


    /**
     * 目录
     */
    private String directory;


}
