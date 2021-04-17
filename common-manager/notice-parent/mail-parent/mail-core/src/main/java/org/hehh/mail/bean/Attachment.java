package org.hehh.mail.bean;

import lombok.Data;
import lombok.NonNull;

import java.io.InputStream;

/**
 * @author: HeHui
 * @create: 2020-03-19 11:37
 * @description: 邮件附件
 **/
@Data
public class Attachment {


    private String fileName;


    private InputStream file;


}
