package org.hehh.file.upload;

/**
 * @author: HeHui
 * @date: 2021-01-27 15:10
 * @description: 上传文件请求参数
 */
public class UploadFileReq {

    /**
     * 用户
     */
    private String user;

    /**
     * 用户身份
     */
    private String userIdentity;

    /**
     * 源
     */
    private String source;


    public UploadFileReq(){}

    public UploadFileReq(String user, String userIdentity, String source) {
        this.user = user;
        this.userIdentity = userIdentity;
        this.source = source;
    }


    /**
     * 获取用户
     *
     * @return {@link String}
     */
    public String getUser() {
        return user;
    }

    /**
     * 设置用户
     *
     * @param user 用户
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 获取用户身份
     *
     * @return {@link String}
     */
    public String getUserIdentity() {
        return userIdentity;
    }

    /**
     * 设置用户身份
     *
     * @param userIdentity 用户身份
     */
    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    /**
     * 得到源
     *
     * @return {@link String}
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置源
     *
     * @param source 源
     */
    public void setSource(String source) {
        this.source = source;
    }
}
