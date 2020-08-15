package org.hehh.utils.http;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.hehh.utils.http.response.HttpURLResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author: HeHui
 * @date: 2020-08-11 00:18
 * @description: http 请求工具
 */
public class HttpClient {


    public static  Map<String,Integer> codeMap;

    static {
        Map<String,Integer> staticCodeMap = new HashMap<>();
        staticCodeMap.put("Continue", 100);
    }


    /**
     * 连接超时
     */
    private int connectTimeout = 3000;

    /**
     * 读取超时
     */
    private int readTimeout = 10000;


    public HttpClient(){}


    public HttpClient(int connectTimeout,int readTimeout){
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }







    /**
     * post body 请求
     *
     * @param url url
     * @return {@link HttpURLResponse}* @throws IOException IoException
     */
    public HttpURLResponse postBody(String url) throws IOException {
        return this.get(url, "{}");
    }



    /**
     *  post body 请求
     *
     * @param url   url
     * @param body 参数
     * @return {@link HttpURLResponse}* @throws IOException IoException
     */
    public HttpURLResponse post(String url,String body) throws IOException {
        return this.get(url,body,null,connectTimeout,readTimeout);
    }


    /**
     * post 表单请求
     *
     * @param url url
     * @return {@link HttpURLResponse}* @throws IOException IoException
     */
    public HttpURLResponse post(String url) throws IOException {
        return this.get(url, Collections.emptyMap());
    }



    /**
     *  post 表单请求
     *
     * @param url   url
     * @param param 参数
     * @return {@link HttpURLResponse}* @throws IOException IoException
     */
    public HttpURLResponse post(String url,Map<String,String> param) throws IOException {
        return this.get(url,param,null,connectTimeout,readTimeout);
    }




    /**
     * post body 请求
     *
     * @param url            url
     * @param body           身体
     * @param headers        头
     * @param connectTimeout 连接超时
     * @param readTimeout    读取超时
     * @return {@link InputStream}* @throws IOException IoException
     */
    public HttpURLResponse post(String url,String body,Headers headers,int connectTimeout, int readTimeout) throws IOException {
        return execute(url,body,headers,connectTimeout,readTimeout,"POST", Headers.JSON);
    }



    /**
     *  post 表单请求
     *
     * @param url            url
     * @param param          参数
     * @param headers        头
     * @param connectTimeout 连接超时
     * @param readTimeout    读取超时
     * @return {@link InputStream}* @throws IOException IoException
     */
    public HttpURLResponse post(String url,Map<String,String> param,Headers headers,int connectTimeout, int readTimeout) throws IOException {
        return execute(url,fromParam(param),headers,connectTimeout,readTimeout,"POST", Headers.DEFAULT);
    }





    /**
     * get body 请求
     *
     * @param url url
     * @return {@link HttpURLResponse}* @throws IOException IoException
     */
    public HttpURLResponse getBody(String url) throws IOException {
        return this.get(url, "{}");
    }



    /**
     *  get body 请求
     *
     * @param url   url
     * @param body 参数
     * @return {@link HttpURLResponse}* @throws IOException IoException
     */
    public HttpURLResponse get(String url,String body) throws IOException {
        return this.get(url,body,null,connectTimeout,readTimeout);
    }


    /**
     * get 表单请求
     *
     * @param url url
     * @return {@link HttpURLResponse}* @throws IOException IoException
     */
    public HttpURLResponse get(String url) throws IOException {
        return this.get(url, Collections.emptyMap());
    }



    /**
     *  get 表单请求
     *
     * @param url   url
     * @param param 参数
     * @return {@link HttpURLResponse}* @throws IOException IoException
     */
    public HttpURLResponse get(String url,Map<String,String> param) throws IOException {
        return this.get(url,param,null,connectTimeout,readTimeout);
    }




    /**
     * get body 请求
     *
     * @param url            url
     * @param body           身体
     * @param headers        头
     * @param connectTimeout 连接超时
     * @param readTimeout    读取超时
     * @return {@link InputStream}* @throws IOException IoException
     */
    public HttpURLResponse get(String url,String body,Headers headers,int connectTimeout, int readTimeout) throws IOException {
        return execute(url,body,headers,connectTimeout,readTimeout,"GET", Headers.JSON);
    }



    /**
     *  get 表单请求
     *
     * @param url            url
     * @param param          参数
     * @param headers        头
     * @param connectTimeout 连接超时
     * @param readTimeout    读取超时
     * @return {@link InputStream}* @throws IOException IoException
     */
    public HttpURLResponse get(String url,Map<String,String> param,Headers headers,int connectTimeout, int readTimeout) throws IOException {
        return execute(url,fromParam(param),headers,connectTimeout,readTimeout,"GET", Headers.DEFAULT);
    }




    /**
     * 执行
     *
     * @param url            url
     * @param data           数据
     * @param headers        头
     * @param connectTimeout 连接超时
     * @param readTimeout    读取超时
     * @param method         方法
     * @param contentType    内容类型
     * @return {@link InputStream}* @throws IOException IoException
     */
    public HttpURLResponse execute(String url, String data, Headers headers, int connectTimeout, int readTimeout, String method, String contentType) throws IOException {

        OutputStreamWriter osw = null;
        try {
            URL realUrl = new URL(url);
            /**
             * 打开和URL之间的连接
             */
            HttpURLConnection con = (HttpURLConnection) realUrl.openConnection();
            con.setRequestMethod(method);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            /**
             *  设置通用请求头
             */
            con.setRequestProperty("accept", "*/*");
            con.setRequestProperty("connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", StrUtil.isBlank(contentType) ? Headers.DEFAULT : contentType);
            con.setRequestProperty("User-Agent", Headers.USER_AGENT);
            if (MapUtil.isNotEmpty(headers)) {
                headers.forEach((k, v) -> {
                    con.setRequestProperty(k, v);
                });
            }

            con.setDoOutput(true);
            con.setDoInput(true);

            /**
             * 获取URLConnection对象对应的输出流
             */
            osw = new OutputStreamWriter(con.getOutputStream(), Charset.defaultCharset());
            if (StrUtil.isNotBlank(data)) {
                /**
                 * 发送请求参数
                 */
                osw.write(data);
            }

            /**
             * flush输出流的缓冲
             */
            osw.flush();
            String msg = con.getResponseMessage();
            Map<String, List<String>> headerFields = con.getHeaderFields();
            InputStream stream = null;
            int code = con.getResponseCode();
            if(StrUtil.isBlank(msg) || msg.equalsIgnoreCase("ok")){
               stream = con.getInputStream();
            }else{
                stream = con.getErrorStream();
            }
            HttpURLResponse response = new HttpURLResponse(stream);
            response.setCode(code);
            response.setMsg(msg);
            response.setHeaders(headerFields);

            return response;
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    osw = null;
                    throw new RuntimeException(e);
                }
            }
        }
    }






    /**
     * 表单参数
     *
     * @param params 参数个数
     * @return {@link String}
     */
    public String fromParam(Map<String,String> params){

        if (params != null && params.size() > 0) {
            StringBuffer sbParams = new StringBuffer();
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()){
                String k = iterator.next();
                sbParams.append(k);
                sbParams.append("=");
                sbParams.append(params.get(k));
                if(iterator.hasNext()){
                    sbParams.append("&");
                }
            }
            return sbParams.toString();
        }
        return null;
    }



    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient(10000,20000);
        HttpURLResponse response = client.getBody("https://bbs.halo.run/api/discussions/958");
        String body = response.body();

        System.out.println(body);
    }



}
