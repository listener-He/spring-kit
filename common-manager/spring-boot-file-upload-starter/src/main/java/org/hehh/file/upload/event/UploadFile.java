package org.hehh.file.upload.event;

import org.hehh.file.upload.req.UploadBase;

/**
 * @author: HeHui
 * @date: 2021-01-27 16:45
 * @description: 上传单个文件
 */
public class UploadFile extends UploadBase {


    private static final long serialVersionUID = -7140740649554880284L;
    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件hash
     */
    private String key;


    public UploadFile() {
        super();
    }

    public UploadFile(UploadBase upload) {
        super(upload);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
