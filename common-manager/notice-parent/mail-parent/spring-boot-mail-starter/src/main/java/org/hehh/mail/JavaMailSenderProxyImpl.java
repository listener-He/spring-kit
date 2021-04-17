package org.hehh.mail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.common.bean.result.ErrorResult;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.mail.bean.Account;
import org.hehh.mail.bean.Mail;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @author: HeHui
 * @create: 2020-03-19 11:45
 * @description: 基于JavaMailSenderImpl委托实现
 **/
@Slf4j
public class JavaMailSenderProxyImpl implements MailSender {



    private final JavaMailSender mailSender;

    private final String sendName;


    /**
     *  委托类构造器
     * @param mailSender
     */
    public JavaMailSenderProxyImpl(JavaMailSenderImpl mailSender){
        Assert.notNull(mailSender,"邮件发送委托类不能为空");
        this.mailSender = mailSender;
        this.sendName = this.getMailSendFrom(mailSender);
    }


    /**
     *  账户构造器
     * @param account
     */
    public JavaMailSenderProxyImpl(Account account){
        Assert.notNull(account,"邮件账户不能为空");
        this.mailSender = createJavaMailSenderImpl(account);
        this.sendName = account.getProperties().getFrom();
    }









    /**
     * 发送邮件
     *
     * @param mail
     * @return
     */
    @Override
    public Result send(Mail mail) {
        return sendMail(mail,mailSender,sendName);
    }



    /**
     * 指定账户发送
     *
     * @param mail
     * @param account
     * @return
     */
    @Override
    public Result send(Mail mail, Account account) {
        if(null == account){
            return ErrorResult.error("发送人信息为空");
        }
        if(!account.validation()){
            return ErrorResult.error("发送人信息非法");

        }

        JavaMailSenderImpl javaMailSenderImpl = createJavaMailSenderImpl(account);

        if(null == javaMailSenderImpl){
            return ErrorResult.error("构建方法发送邮箱对象失败!");
        }
        return sendMail(mail,javaMailSenderImpl,account.getProperties().getFrom());
    }



    /**
     * 默认邮箱异步发送
     *
     * @param mail
     */
    @Async
    @Override
    public void setAsync(Mail mail) {
        this.sendMail(mail,mailSender,sendName);
    }







    /**
     *  获取发送人
     * @param javaMailSender
     * @return
     */
    private String getMailSendFrom(JavaMailSenderImpl javaMailSender) {
        return javaMailSender.getJavaMailProperties().getProperty("from");
    }




    /**
     *  构建发送对象
     * @param emailAccount
     * @return
     */
    private JavaMailSenderImpl createJavaMailSenderImpl(Account emailAccount){


        try {

            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(emailAccount.getHost());
            javaMailSender.setPort(emailAccount.getPort());
            javaMailSender.setUsername(emailAccount.getUsername());
            javaMailSender.setPassword(emailAccount.getPassword());
            javaMailSender.setDefaultEncoding("UTF-8");


            Properties p = new Properties();
            p.setProperty("from", emailAccount.getProperties().getFrom());
            p.setProperty("mail.smtp.socketFactory.class",emailAccount.getProperties().getMail().getSmtp().getSocketFactory().getClazz());
            p.setProperty("mail.smtp.socketFactory.socketFactory.port",emailAccount.getProperties().getMail().getSmtp().getSocketFactory().getPort()+"");
            p.setProperty("mail.smtp.autht",Boolean.valueOf(emailAccount.getProperties().getMail().isAuth()).toString());

            javaMailSender.setJavaMailProperties(p);
            return javaMailSender;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }
















    /**
     *  发送邮件
     * @param mailVo
     */

    private Result sendMail(Mail mailVo,JavaMailSender javaMailSender,String sendFrom) {
        if(null == mailVo){
            return ErrorResult.error("邮件信息不得null");
        }
        if(!mailVo.checkLegal()){
            log.error("邮件信息,{}",mailVo);
            return ErrorResult.error("未满足发送条件,无法发送邮件");
        }
        try {

            /**正式发送邮件*/
            javaMailSender.send(binderMessage(mailVo, javaMailSender, sendFrom));
            return mailVo.toResult();
        } catch (Exception e) {
            e.printStackTrace();
            return mailVo.toResult();
        }
    }


    /**
     *  构建消息
     * @param mailVo
     * @param javaMailSender
     * @param sendFrom
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private MimeMessage binderMessage(Mail mailVo, JavaMailSender javaMailSender, String sendFrom) throws MessagingException, UnsupportedEncodingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);//true表示支持复杂类型
        /**邮件发信人从配置项读取*/

        if(StrUtil.isBlank(mailVo.getSendUserName())){
            /**邮件发信人*/
            messageHelper.setFrom(sendFrom);
        }else{
            /**邮件发信人*/
            messageHelper.setFrom(sendFrom,mailVo.getSendUserName());
        }

        /**邮件收信人*/
        messageHelper.setTo(ArrayUtil.toArray(mailVo.getReceiveEmail(),String.class));

        /**邮件主题*/
        messageHelper.setSubject(mailVo.getSubject());


        /**邮件内容*/
        messageHelper.setText(mailVo.getEmailContent(),mailVo.getIsHtml());
        if (CollUtil.isNotEmpty(mailVo.getFuckSend())) {
            /**抄送*/
            messageHelper.setCc(ArrayUtil.toArray(mailVo.getFuckSend(),String.class));
        }
        if (CollUtil.isNotEmpty(mailVo.getSecretSend())) {
            /**密送*/
            messageHelper.setCc(ArrayUtil.toArray(mailVo.getSecretSend(),String.class));
        }

        /**
         *  附件信息
         */
        if (CollUtil.isNotEmpty(mailVo.getAttachments())) {
            mailVo.getAttachments().forEach(file->{
                try {
                    /**添加邮件附件*/
                    messageHelper.addAttachment(file.getFileName(), new ByteArrayResource(IoUtil.readBytes(file.getFile())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }


        if (null == mailVo.getSentDate()) {//发送时间
            mailVo.setSentDate(new Date());
        }
        messageHelper.setSentDate(mailVo.getSentDate());
        return messageHelper.getMimeMessage();
    }






//    /**
//     * 处理二进制邮件的基本信息，比如需要带附件的文本邮件、HTML文件、图片邮件、模板邮件等等
//     *
//     * @param mimeMessageHelper：二进制文件的包装类
//     * @param subject：邮件主题
//     * @param content：邮件内容
//     * @param toWho：收件人
//     * @param ccPeoples：抄送人
//     * @param bccPeoples：暗送人
//     * @param isHtml：是否是HTML文件，用于区分带附件的简单文本邮件和真正的HTML文件
//     *
//     * @return ：返回这个过程中是否出现异常，当出现异常时会取消邮件的发送
//     */
//    private boolean handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, boolean isHtml){
//
//        try{
//            //设置必要的邮件元素
//
//            //设置发件人
//            mimeMessageHelper.setFrom(getMailSendFrom());
//            //设置邮件的主题
//            mimeMessageHelper.setSubject(subject);
//            //设置邮件的内容，区别是否是HTML邮件
//            mimeMessageHelper.setText(content,isHtml);
//            //设置邮件的收件人
//            mimeMessageHelper.setTo(toWho);
//
//            //设置非必要的邮件元素，在使用helper进行封装时，这些数据都不能够为空
//
//            if(ccPeoples != null)
//                //设置邮件的抄送人：MimeMessageHelper # Assert.notNull(cc, "Cc address array must not be null");
//                mimeMessageHelper.setCc(ccPeoples);
//
//            if(bccPeoples != null)
//                //设置邮件的密送人：MimeMessageHelper # Assert.notNull(bcc, "Bcc address array must not be null");
//                mimeMessageHelper.setBcc(bccPeoples);
//
//            return true;
//        }catch(MessagingException e){
//            e.printStackTrace();
//
//            log.error("邮件基本信息出错->{}",subject);
//        }
//
//
//        return false;
//    }
//
//
//
//
//    /**
//     * 用于处理附件信息，附件需要 MimeMessage 对象
//     *
//     * @param mimeMessageHelper：处理附件的信息对象
//     * @param subject：邮件的主题，用于日志记录
//     * @param attachmentFilePaths：附件文件的路径，该路径要求可以定位到本机的一个资源
//     */
//    private void handleAttachment(MimeMessageHelper mimeMessageHelper,String subject,String[] attachmentFilePaths){
//
//        //判断是否需要处理邮件的附件
//        if(attachmentFilePaths != null&&attachmentFilePaths.length > 0){
//
//            FileSystemResource resource;
//
//            String fileName;
//
//            //循环处理邮件的附件
//            for(String attachmentFilePath : attachmentFilePaths){
//
//                //获取该路径所对应的文件资源对象
//                resource = new FileSystemResource(new File(attachmentFilePath));
//
//                //判断该资源是否存在，当不存在时仅仅会打印一条警告日志，不会中断处理程序。
//                // 也就是说在附件出现异常的情况下，邮件是可以正常发送的，所以请确定你发送的邮件附件在本机存在
//                if(!resource.exists()){
//
//                    log.warn("邮件->{} 的附件->{} 不存在！",subject,attachmentFilePath);
//
//                    //开启下一个资源的处理
//                    continue;
//                }
//
//                //获取资源的名称
//                fileName = resource.getFilename();
//
//                try{
//
//                    //添加附件
//                    mimeMessageHelper.addAttachment(fileName,resource);
//
//                }catch(MessagingException e){
//
//                    e.printStackTrace();
//
//                    log.error("邮件->{} 添加附件->{} 出现异常->{}",subject,attachmentFilePath,e.getMessage());
//                }
//            }
//        }
//    }
}
