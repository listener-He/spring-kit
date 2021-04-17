package org.hehh.utils.http;


import java.util.HashMap;

/**
 * @author: HeHui
 * @date: 2020-08-06 18:59
 * @description: 请求头
 */
public class Headers extends HashMap<String,String> {

    /**
     *  默认 user-agent 头
     */
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Windows NT 5.1;SV1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36";

    /**
     *  请求content-type
     */
    public static final String JSON = "application/json; charset=utf-8";
    public static final String XML = "application/xml; charset=utf-8";
    public static final String DEFAULT = "application/x-www-form-urlencoded; charset=utf-8";


}
