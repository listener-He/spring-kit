package org.hehh.file.upload.event;

import org.hehh.file.upload.UploadFileReq;

/**
 * @author: HeHui
 * @date: 2021-01-27 15:45
 * @description: 上传事件
 */
public class UploadEvent implements java.io.Serializable {


    private static final long serialVersionUID = -29201897261693493L;
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




    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    /**
     * 设置上传用户
     *
     * @param req 要求的事情
     */
    public void settingUploadUser(UploadFileReq req) {
        this.source = req.getSource();
        this.user = req.getUser();
        this.userIdentity = req.getUserIdentity();
    }
}
