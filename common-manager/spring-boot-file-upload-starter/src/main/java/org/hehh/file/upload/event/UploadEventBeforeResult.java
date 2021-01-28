package org.hehh.file.upload.event;

/**
 * @author: HeHui
 * @date: 2021-01-28 11:42
 * @description: 上传事件前置返回
 */
public class UploadEventBeforeResult extends UploadEventResult {

    private static final long serialVersionUID = 6715919098375619312L;


    /**
     * 是否重复
     */
    private boolean repeat = false;


    /**
     * 重复的url
     */
    private String repeatUrl;


    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getRepeatUrl() {
        return repeatUrl;
    }

    public void setRepeatUrl(String repeatUrl) {
        this.repeatUrl = repeatUrl;
    }

    /**
     * 释放
     *
     * @param release 释放
     *
     * @return {@link UploadEventBeforeResult}
     */
    @Override
    public UploadEventBeforeResult release(boolean release) {
        super.release(release);
        return this;
    }

    /**
     * 重复
     *
     * @param repeat 重复
     *
     * @return {@link UploadEventBeforeResult}
     */
    public UploadEventBeforeResult repeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    /**
     * url
     *
     * @param url url
     *
     * @return {@link UploadEventBeforeResult}
     */
    public UploadEventBeforeResult url(String url) {
        this.repeatUrl = url;
        return this;
    }


    /**
     * 构建 用来隐藏父
     *
     * @return {@link UploadEventBeforeResult}
     */
    public static UploadEventBeforeResult build() {
        return new UploadEventBeforeResult();
    }

    /**
     * 重复
     *
     * @param url url
     *
     * @return {@link UploadEventBeforeResult}
     */
    public static UploadEventBeforeResult repeat(String url) {
        return repeat(url, true);
    }

    /**
     * 重复
     *
     * @param url     url
     * @param release 释放
     *
     * @return {@link UploadEventBeforeResult}
     */
    public static UploadEventBeforeResult repeat(String url, boolean release) {
        return build().url(url).release(release);
    }
}
