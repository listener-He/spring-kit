package org.hehh.cloud.spring.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author: HeHui
 * @date: 2020-09-12 16:09
 * @description: 签名工具类
 */
@Slf4j
public class SignUtil {


    /**
     * 字符编码
     */
    public final static String INPUT_CHARSET = "UTF-8";

    /**
     * 超时时间
     */
    private final static int TIME_OUT = 5 * 60 * 1000;


    /**
     * 请求参数Map转换验证Map
     *
     * @param requestParams 请求参数Map
     * @param charset       是否要转utf8编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> toVerifyMap(Map<String, String[]> requestParams, boolean charset) {
        if (CollectionUtils.isEmpty(requestParams)) {
            return null;
        }
        Map<String, String> params = new HashMap<>(requestParams.size());
        Iterator<String> keys = requestParams.keySet().iterator();
        while (keys.hasNext()) {
            String name =  keys.next();
            String[] values = requestParams.get(name);
            String valueStr = null;

            if(values != null && values.length > 0){
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : (valueStr + values[i] + ",");
                }
            }

            /**
             * 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
             */
            if (charset && valueStr != null) {
                valueStr = getContentString(valueStr, INPUT_CHARSET);
            }

            params.put(name, valueStr);
        }

        return params;
    }


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        return createLinkString(params, false);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否需要UrlEncode
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean encode) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                value = urlEncode(value, INPUT_CHARSET);
            }
            //拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }


    /**
     * 编码转换
     *
     * @param content
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (StringUtils.isEmpty(charset)) {
            return null;
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


    /**
     * URL转码
     *
     * @param content
     * @param charset
     * @return
     */
    public static String urlEncode(String content, String charset) {
        try {
            return URLEncoder.encode(content, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 编码转换
     *
     * @param content
     * @param charset
     * @return
     */
    public static String getContentString(String content, String charset) {
        if (StringUtils.isEmpty(charset)) {
            return null;
        }
        try {
            return new String(content.getBytes(), charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


    /**
     * base64编码str
     *
     * @param str str
     * @return {@link String}
     */
    public static String encodeBase64Str(byte[] str) {
        try {
            String result = new String(Base64.getEncoder().encode(str), INPUT_CHARSET);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * base64解码
     *
     * @param str str
     * @return {@link byte[]}
     */
    public static byte[] decodeBase64(String str) {
        try {
            byte[] result = Base64.getDecoder().decode(str.getBytes(INPUT_CHARSET));
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
