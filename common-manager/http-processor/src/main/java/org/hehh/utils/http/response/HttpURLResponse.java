package org.hehh.utils.http.response;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-15 16:40
 * @description: HTTP URL 响应
 */
public class HttpURLResponse implements ResponseProxy {

    /**
     * 状态码
     */
    private int code;

    private String msg;

    private Map<String,List<String>> headers;


    private InputStream inputStream;



    private Object data;

    private String body;




    /**
     * http urlresponse
     *
     * @param inputStream 输入流
     */
    public HttpURLResponse(InputStream inputStream){
        this.inputStream = inputStream;
    }


    /**
     * 获得地位
     *
     * @return int
     */
    @Override
    public int getStatus() {
        return code;
    }

    /**
     * 获取数据 调用body方法然后json转换
     *
     * @param tClass
     * @return {@link T}
     * @see ResponseProxy#body()
     */
    @Override
    public <T> T getData(Class<T> tClass) throws IOException {
        if (this.data == null) {
            if(tClass.isAssignableFrom(InputStream.class)){
                this.data = inputStream;
            }else{
                String value = body();
                if (String.class.equals(tClass)) {
                    this.data = (T) value;
                } else if (Number.class.isAssignableFrom(tClass)) {
                    this.data = (T) NumberUtil.parseNumber(value);
                } else {
                    this.data = JSONUtil.toBean(value, tClass);
                }
            }
        }
        return (T) this.data;
    }


    /**
     * 主体数据 json/body
     */
    @Override
    public String body() throws IOException {
        if (this.body == null) {
            if(inputStream != null){
                this.body = readString(inputStream);
            }
        }
        return this.body;
    }


    /**
     * 得到响应头
     *
     * @return {@link Map <String,  List <String>>}
     */
    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * 获取响应 cookie
     *
     * @return {@link List< HttpCookie >}
     */
    @Override
    public List<HttpCookie> getCookies() {
        return null;
    }





    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }






    /**
     * 读取字符串
     *
     * @param stream 流
     * @return {@link String}* @throws IOException ioexception
     */
    public  String readString(InputStream stream) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(stream,Charset.defaultCharset()));
            StringBuffer result = new StringBuffer();

            String temp;
            while ((temp = br.readLine()) != null) {
                result.append(temp);
            }
            return result.toString();

        }finally {
            stream.close();
            if(br != null){
                br.close();
            }
        }

    }
}
