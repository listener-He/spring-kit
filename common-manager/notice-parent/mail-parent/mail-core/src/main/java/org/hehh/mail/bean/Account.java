package org.hehh.mail.bean;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-08-16 23:06
 * @description: 账户
 */
@Data
public class Account {

    /**
     * SMTP服务器地址
     */
    private String host = "smtp.mxhichina.com";

    /**
     * 登陆名
     */
    private String username;

    /**
     * 端口
     */
    private int port = 465;

    /**
     * 密码
     */
    private String password;


    /**
     * 参数
     */
    private Properties properties = new Properties();


    /**
     * 属性
     *
     * @author hehui
     * @date 2020/08/16
     */
    @Data
    public static class Properties {
        /**
         * 邮件发信人（即真实邮箱）
         */
        private String from;

        private Mail mail = new Mail();

        @Data
        public static class Mail {

            private boolean auth = true;


            private Smtp smtp = new Smtp();


            @Data
            public static class Smtp {
                private SocketFactory socketFactory = new SocketFactory();

                private Starttls Starttls = new Starttls();


                @Data
                public static class Starttls {
                    private boolean enable = true;
                    private boolean required = true;
                }


                @Data
                public static class SocketFactory {
                    private String clazz = "javax.net.ssl.SSLSocketFactory";

                    private int port = 465;
                }
            }
        }
    }



    /**
     * 校验参数
     *
     * @return
     */
    public boolean validation() {
        if (StrUtil.isBlank(username) || null == properties || StrUtil.isBlank(properties.from) || StrUtil.isBlank(password)) {
            return false;
        }

        return true;
    }
}
