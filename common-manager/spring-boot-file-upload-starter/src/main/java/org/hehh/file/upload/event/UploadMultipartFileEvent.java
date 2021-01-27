package org.hehh.file.upload.event;

/**
 * @author: HeHui
 * @date: 2021-01-27 16:45
 * @description: 上传单个文件事件
 */
public class UploadMultipartFileEvent extends UploadEvent {


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
