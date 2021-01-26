package org.hehh.file.upload.event;


/**
 * @author: HeHui
 * @date: 2021-01-26 13:48
 * @description: 上传文件事件
 */
public interface UploadFileEvent {


    /**
     * 保存前
     *
     * @param fileId   文件标识
     * @param filename 文件名
     */
    void before(String fileId,String filename);

    /**
     * 异常
     *
     * @param fileId    文件标识
     * @param exception 异常
     */
    void error(String fileId,Exception exception);

    /**
     *
     *  后
     * @param fileId 文件标识
     * @param url    url
     */
    void after(String fileId,String url);
}
