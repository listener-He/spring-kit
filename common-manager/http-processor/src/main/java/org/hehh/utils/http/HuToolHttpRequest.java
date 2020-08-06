package org.hehh.utils.http;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:04
 * @description: hutool 的 http 请求
 */
public class HuToolHttpRequest implements HttpRequest {


    /**
     * get 请求
     *
     * @param urlString    url字符串
     * @param responseType 响应类型
     * @param headers      请求头
     * @param timeout      超时毫秒
     * @return {@link T}
     */
    @Override
    public <T> T get(String urlString, Class<T> responseType, Headers headers, int timeout) {
        cn.hutool.http.HttpRequest request = HttpUtil.createGet(urlString);
        if(headers != null){
            request.headerMap(headers,true);
        }

        request.setReadTimeout(timeout);
        HttpResponse response = request.execute();
        if(response.isOk()){
            String body = response.body();
            return JSONUtil.toBean(body,responseType);
        }
        return null;
    }





    /**
     * post 请求
     *
     * @param url          url
     * @param param        参数
     * @param headers
     * @param responseType
     * @param timeout      超时
     * @return {@link T}
     */
    @Override
    public <T> T post(String url, Map<String, Object> param, Headers headers, Class<T> responseType, int timeout) {
        cn.hutool.http.HttpRequest request = HttpUtil.createPost(url);

        if(headers != null){
            request.headerMap(headers,true);
        }
        request.form(param);

        request.setReadTimeout(timeout);
        HttpResponse response = request.execute();
        if(response.isOk()){
            String body = response.body();
            return JSONUtil.toBean(body,responseType);
        }
        return null;
    }




    /**
     * post 请求
     *
     * @param url          url
     * @param body         请求参数
     * @param headers      请求头
     * @param responseType
     * @param timeout      超时
     * @return {@link T}
     */
    @Override
    public <T> T post(String url, String body, Headers headers, Class<T> responseType, int timeout) {
        cn.hutool.http.HttpRequest request = HttpUtil.createPost(url);

        if(headers != null){
            request.headerMap(headers,true);
        }
        request.body(body);

        request.setReadTimeout(timeout);
        HttpResponse response = request.execute();
        if(response.isOk()){
            return JSONUtil.toBean(response.body(),responseType);
        }
        return null;
    }
}
