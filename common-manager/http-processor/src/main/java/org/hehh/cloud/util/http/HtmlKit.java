package org.hehh.cloud.util.http;


import org.hehh.cloud.common.utils.StrKit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: HeHui
 * @create: 2019-12-13 09:21
 * @description: html 文本工具类
 **/
public class HtmlKit {


    /**
     *  图片url可能存放的地方
     */
    private final static String[] IMG_URL_NAME = {"src","data-original","originalsrc"};



    /**
     *   获取元素属性
     * @param src
     * @param key
     * @return
     */
    public static String getElementAttribute(String src, String key) {
        Pattern pattern = Pattern.compile("(?:" + key + "\\s*=\\s*)" + "['\"](.*?)['\"]");
        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            return matcher.group().replaceAll(key + "\\s*=\\s*", "").replaceAll("\"", "");
        }
        return null;
    }


    /**
     *  获取图片url
     * @param html html文本
     * @return
     */
    public static String getImgUrl(String html){
        for (String s : IMG_URL_NAME) {
            String attribute = getElementAttribute(html, s);
            if(StrKit.isUrl(attribute)){
                return attribute;
            }
        }

        return null;
    }




}
