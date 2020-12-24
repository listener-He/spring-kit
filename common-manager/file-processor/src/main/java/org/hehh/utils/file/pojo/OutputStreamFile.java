package org.hehh.utils.file.pojo;

import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.attribute.FileTime;

/**
 * @author: HeHui
 * @date: 2020-12-22 19:39
 * @description: 输出流文件
 */
@Data
public class OutputStreamFile {

    /**
     * 流
     */
    private ByteArrayOutputStream stream;

    /**
     * 的名字
     */
    private String name;

    /**
     * 是否目录
     */
    private boolean directory = false;

    /**
     * 大小
     */
    private long size = 0;

    /**
     * 最后访问时间
     */
    private Long lastAccessTime;

    /**
     * 创建时间
     */
    private Long creationTime;

    /**
     * 最后修改时间
     */
    private Long lastModifiedTime;


    /**
     * 文件输出流
     */
    public OutputStreamFile(){}
    /**
     * 输入流文件
     *
     * @param stream 流
     * @param name   的名字
     */
    public OutputStreamFile(ByteArrayOutputStream stream, String name) {
        this.stream = stream;
        this.name = name;
    }


}
