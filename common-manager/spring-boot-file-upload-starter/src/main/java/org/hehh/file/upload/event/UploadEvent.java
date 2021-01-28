package org.hehh.file.upload.event;

/**
 * @author: HeHui
 * @date: 2021-01-28 13:44
 * @description: 上传事件
 */
public class UploadEvent {

    private Object upload;

    public UploadEvent() {
    }

    public <T extends UploadBase> UploadEvent(T upload) {
        this.upload = upload;
    }

    public <T extends UploadBase> T getUpload(Class<T> tClass) {
        return (T) upload;
    }

    public <T extends UploadBase> void setUpload(T upload) {
        this.upload = upload;
    }

    public <T extends UploadBase> UploadEvent upload(T upload) {
        this.upload = upload;
        return this;
    }


    public <T extends UploadBase> boolean uploadClass(Class<T> tClass) {
        return this.upload.getClass().isAssignableFrom(tClass);
    }


}
