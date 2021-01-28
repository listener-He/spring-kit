package org.hehh.file.upload.event;

/**
 * @author: HeHui
 * @date: 2021-01-28 11:51
 * @description: 上传事件完成返回
 */
public class UploadEventAfterResult extends UploadEventResult {

    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UploadEventAfterResult url(String url) {
        setUrl(url);
        return this;
    }

    @Override
    public UploadEventAfterResult release(boolean release) {
        super.release(release);
        return this;
    }

    /**
     * 构建 用来隐藏父
     *
     * @return {@link UploadEventBeforeResult}
     */
    public static UploadEventAfterResult result(String url) {
        return new UploadEventAfterResult().url(url);
    }

    /**
     * 结果
     *
     * @param url     url
     * @param release 释放
     *
     * @return {@link UploadEventAfterResult}
     */
    public static UploadEventAfterResult result(String url, boolean release) {
        return new UploadEventAfterResult().url(url).release(release);
    }
}
