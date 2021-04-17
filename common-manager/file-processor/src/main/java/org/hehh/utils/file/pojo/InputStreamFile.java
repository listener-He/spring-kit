package org.hehh.utils.file.pojo;

import java.io.InputStream;

/**
 * @author: HeHui
 * @date: 2020-12-22 19:39
 * @description: 输入流文件
 */
public class InputStreamFile {

    private InputStream stream;

    private String name;


    /**
     * 输入流文件
     *
     * @param stream 流
     * @param name   的名字
     */
    public InputStreamFile(InputStream stream, String name) {
        this.stream = stream;
        this.name = name;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
