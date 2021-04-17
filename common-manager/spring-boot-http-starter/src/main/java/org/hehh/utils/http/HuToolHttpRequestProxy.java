package org.hehh.utils.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.HuToolResponseProxy;
import org.hehh.utils.http.response.ResponseProxy;

import java.io.File;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:04
 * @description: hutool 的 http 请求
 */
public class HuToolHttpRequestProxy implements HttpRequestProxy {



    /**
     * get 请求
     *
     * @param urlString    url字符串
     * @param headers      请求头
     * @param timeout      超时毫秒
     */
    @Override
    public  ResponseProxy get(String urlString,  Headers headers, int timeout) {
        cn.hutool.http.HttpRequest request = HttpUtil.createGet(urlString);
        if(headers != null){
            request.headerMap(headers,true);
        }
        request.setReadTimeout(timeout);
        return new HuToolResponseProxy(request.execute());
    }





    /**
     * post 请求
     *
     * @param url          url
     * @param param        参数
     * @param headers
     * @param timeout      超时
     */
    @Override
    public  ResponseProxy post(String url, Map<String, String> param, Headers headers,  int timeout) {
        cn.hutool.http.HttpRequest request = HttpUtil.createPost(url);

        if(headers != null){
            request.headerMap(headers,true);
        }
        request.form((Map)param);

        request.setReadTimeout(timeout);



        return new HuToolResponseProxy(request.execute());
    }




    /**
     * post 请求
     *
     * @param url          url
     * @param body         请求参数
     * @param headers      请求头
     * @param timeout      超时
     */
    @Override
    public  ResponseProxy post(String url, String body, Headers headers,  int timeout) {
        cn.hutool.http.HttpRequest request = HttpUtil.createPost(url);

        if(headers != null){
            request.headerMap(headers,true);
        }
        request.body(body);
        request.setReadTimeout(timeout);
        return new HuToolResponseProxy(request.execute());
    }




    /**
     * 下载 get
     *
     * @param url     url
     * @param headers 头
     * @param file    下载到文件
     * @param timeout 超时
     * @return {@link ResponseProxy<File>}
     */
    @Override
    public ResponseProxy downloadGet(String url, Headers headers, File file, int timeout) {
        cn.hutool.http.HttpRequest request = HttpUtil.createGet(url);

        if(headers != null){
            request.headerMap(headers,true);
        }
        request.setReadTimeout(timeout);
        return download(file, request);
    }



    /**
     * 下载 post
     *
     * @param url     url
     * @param param   参数
     * @param headers 头
     * @param file    下载到文档
     * @param timeout 超时
     * @return {@link ResponseProxy<File>}
     */
    @Override
    public ResponseProxy downloadPost(String url, Map<String, String> param, Headers headers, File file, int timeout) {
        cn.hutool.http.HttpRequest request = HttpUtil.createPost(url);

        if(headers != null){
            request.headerMap(headers,true);
        }
        if(param != null){
            request.form((Map) param);
        }

        request.setReadTimeout(timeout);

        return download(file, request);
    }



    /**
     * 下载
     *
     * @param file    文件
     * @param request 请求
     * @return {@link ResponseProxy<File>}
     */
    private ResponseProxy download(File file, HttpRequest request) {
        HttpResponse execute = request.execute();
        execute.writeBody(file);
        HuToolResponseProxy proxy = new HuToolResponseProxy(execute);
        proxy.setData(file);
        return proxy;
    }
}
