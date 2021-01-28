package org.hehh.file.upload.event;

/**
 * @author: HeHui
 * @date: 2021-01-28 11:46
 * @description: 上传事件异常返回
 */
public class UploadEventErrorResult extends UploadEventResult {

    private String demotionUrl;

    private boolean throwError = true;


    public String getDemotionUrl() {
        return demotionUrl;
    }

    public void setDemotionUrl(String demotionUrl) {
        this.demotionUrl = demotionUrl;
    }

    public boolean isThrowError() {
        return throwError;
    }

    public void setThrowError(boolean throwError) {
        this.throwError = throwError;
    }


    public UploadEventErrorResult url(String url) {
        setDemotionUrl(url);
        return this;
    }

    public UploadEventErrorResult throwError(boolean throwError) {
        setThrowError(throwError);
        return this;
    }

    /**
     * 释放
     *
     * @param release 释放
     *
     * @return {@link UploadEventErrorResult}
     */
    @Override
    public UploadEventErrorResult release(boolean release) {
        super.release(release);
        return this;
    }


    /**
     * 构建 用来隐藏父
     *
     * @return {@link UploadEventErrorResult}
     */
    public static UploadEventErrorResult error() {
        return new UploadEventErrorResult().release(false);
    }

    public static UploadEventErrorResult error(boolean release) {
        return new UploadEventErrorResult().release(release);
    }


    /**
     * 抑制误差
     *
     * @param url url
     *
     * @return {@link UploadEventErrorResult}
     */
    public static UploadEventErrorResult inhibitionError(String url) {
        return new UploadEventErrorResult().url(url).release(false).throwError(false);
    }

    /**
     * 抑制误差
     *
     * @param url     url
     * @param release 释放
     *
     * @return {@link UploadEventErrorResult}
     */
    public static UploadEventErrorResult inhibitionError(String url, boolean release) {
        return new UploadEventErrorResult().url(url).release(release).throwError(false);
    }
}
