package org.hehh.mail;

import org.hehh.cloud.common.bean.result.Result;
import org.hehh.mail.bean.Account;
import org.hehh.mail.bean.Mail;

/**
 * @author: HeHui
 * @date: 2020-08-16 23:08
 * @description: 邮件发送类
 */
public interface MailSender {

    /**
     *  发送邮件
     * @param mail
     * @return
     */
    Result send(Mail mail);


    /**
     *  指定账户发送
     * @param mail
     * @param account
     * @return
     */
    Result send(Mail mail, Account account);


    /**
     *  默认邮箱异步发送
     * @param mail
     */
    void setAsync(Mail mail);
}
