package org.hehh.mail.bean;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.hehh.cloud.common.bean.result.ErrorResult;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.common.bean.result.SuccessResult;
import org.hehh.utils.StrKit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-16 23:03
 * @description: 邮件
 */
@Data
public class Mail {


    /**邮件id*/
    private String id;

    /**邮件接收人*/
    private List<String> receiveEmail;



    /**邮件主题*/
    private String subject;



    /**邮件内容*/
    private String emailContent;

    private Boolean isHtml;

    /**发送时间*/
    private Date sentDate;

    /**抄送*/
    private List<String> fuckSend;

    /**密送*/
    private List<String> secretSend;

    private String sendUserName;

    private String status;

    /**邮件附件*/
    private List<Attachment> attachments;


    public Result toResult(){
        if(StrUtil.isBlank(status)){
            return SuccessResult.succeed(null);
        }

        if(status.startsWith("42")){
            return ErrorResult.error("网络异常");
        }else if(status.startsWith("450")){
            return ErrorResult.error("发送者异常");
        }else if(status.startsWith("451")){
            return ErrorResult.error("连接错误");
        }else if(status.startsWith("550")){
            return ErrorResult.error("找不到发送邮箱");
        }else if(status.startsWith("552")){
            return ErrorResult.error("不允许发送此类附件");
        }else if(status.startsWith("5")){
            return ErrorResult.error("发送错误");
        }
        return SuccessResult.succeed(null);
    }
    /**
     *  添加接收人
     * @param email
     */
    public void addReceiveEmail(String email){
        if(null == receiveEmail){
            receiveEmail = new ArrayList<>();
        }
        receiveEmail.add(email);
    }

    /**
     *  添加抄送人
     * @param email
     */
    public void addSecretSend(String email){
        if(null == secretSend){
            secretSend = new ArrayList<>();
        }
        secretSend.add(email);
    }


    /**
     *  添加抄送人
     * @param email
     */
    public void addFuckSend(String email){
        if(null == fuckSend){
            fuckSend = new ArrayList<>();
        }
        fuckSend.add(email);
    }


    public boolean getIsHtml(){
        return null == isHtml?false:isHtml;
    }


    /**
     * 校验合法性
     * @return
     */
    public boolean checkLegal(){

        if( CollUtil.isEmpty(receiveEmail) || StrUtil.isBlank(subject)||
            StrUtil.isBlank(emailContent)){
            return false;
        }

        return true;
    }



}
