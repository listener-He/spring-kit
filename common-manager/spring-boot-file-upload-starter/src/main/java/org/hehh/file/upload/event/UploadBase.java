package org.hehh.file.upload.event;

import org.hehh.file.upload.UploadFileReq;
import org.springframework.beans.BeanUtils;

/**
 * @author: HeHui
 * @date: 2021-01-27 15:45
 * @description: 上传
 */
public class UploadBase implements java.io.Serializable {


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


    /**
     *  业务赋予url
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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
//        this.source = req.getSource();
//        this.user = req.getUser();
//        this.userIdentity = req.getUserIdentity();
        BeanUtils.copyProperties(req,this);
    }
}
