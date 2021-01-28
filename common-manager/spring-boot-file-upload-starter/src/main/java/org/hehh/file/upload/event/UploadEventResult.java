package org.hehh.file.upload.event;

/**
 * @author: HeHui
 * @date: 2021-01-28 11:16
 * @description: 上传事件返回
 */
public class UploadEventResult implements java.io.Serializable {

    private static final long serialVersionUID = -5347264121400168332L;


    /**
     * 是否放行
     */
    private boolean release = true;




    public boolean isRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }




    public UploadEventResult release(boolean release) {
        this.release = release;
        return this;
    }









    public static UploadEventResult build() {
        return new UploadEventResult();
    }

    public static UploadEventResult refused() {
        return build().release(false);
    }




}
