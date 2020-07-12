package org.hehh.security;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-06-17 11:10
 * @description: ip规则
 */
@Data
public class IpRule implements java.io.Serializable {



    /**
     *  ip表达式
     */
    private String ip;

    /**
     *  cidr后缀
     */
    private String cidrPrefix;
}
