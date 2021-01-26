package org.hehh.file.upload.qiniu;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-22 16:53
 * @description: 七牛存储空间
 */
@Data
public class Bucket {

    /**
     *  存储空间名
     */
    private String name;

    /**
     *  域名访问地址
     */
    private String domain;
}

