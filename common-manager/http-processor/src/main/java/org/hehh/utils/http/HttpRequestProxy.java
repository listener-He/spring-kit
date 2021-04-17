package org.hehh.utils.http;

import org.hehh.utils.http.response.ResponseProxy;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-06 18:58
 * @description: http 请求 代理
 */
public interface HttpRequestProxy {


    /**  get 请求
     *
     * @param urlString    url字符串
     * @throws IOException exception
     */
    default ResponseProxy get(String urlString) throws IOException {
        return get(urlString,-1);
    };



    /**
     *  get 请求
     *
     * @param urlString    url字符串
     * @param timeout 超时毫秒
     * @throws IOException exception
     */
    default  ResponseProxy get(String urlString,int timeout) throws IOException{
        return get(urlString,null,timeout);
    }




    /**
     *  get 请求
     *
     * @param urlString    url字符串
     * @param headers 请求头
     * @param timeout 超时毫秒
     * @throws IOException exception
     */
     ResponseProxy get(String urlString,Headers headers,int timeout) throws IOException;






    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @throws IOException exception
     */
    default  ResponseProxy post(String url, Map<String,String> param) throws IOException{
        return post(url,param,-1);
    };


    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @param timeout 超时
     * @throws IOException exception
     */
    default  ResponseProxy post(String url, Map<String,String> param, int timeout) throws IOException {
        return post(url,param,null,timeout);
    }



    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @param timeout 超时
     * @throws IOException exception
     */
     ResponseProxy post(String url, Map<String,String> param,Headers headers, int timeout) throws IOException;





    /**
     *  post 请求
     *
     * @param url     url
     * @param body 请求参数
     * @throws IOException exception
     */
    default  ResponseProxy post(String url,String body) throws IOException {
        return post(url,body,-1);
    }


    /**
     *  post 请求
     *
     * @param url     url
     * @param body 请求参数
     * @param timeout 超时
     * @throws IOException exception
     */
    default  ResponseProxy post(String url,String body ,int timeout) throws IOException {
        return post(url,body,null,timeout);
    }


    /**
     * post 请求
     *
     * @param url          url
     * @param body         请求参数
     * @param headers      请求头
     * @param timeout      超时
     * @throws IOException exception
     */
     ResponseProxy post(String url,String body ,Headers headers,int timeout) throws IOException;



    /**
     * 下载 get
     *
     * @param url          url
     * @param file 下载到文件
     * @return {@link ResponseProxy<File>}
     */
    default ResponseProxy downloadGet(String url,File file) throws IOException {
        return this.downloadGet(url,null,file,-1);
    }



    /**
     * 下载 get
     *
     * @param url          url
     * @param headers      头
     * @param file 下载到文件
     * @param timeout      超时
     * @return {@link ResponseProxy<File>}
     */
     ResponseProxy downloadGet(String url,Headers headers,File file, int timeout) throws IOException;



    /**
     * 下载 post
     * @param url          url
     * @param file  下载到文档
     * @param param        参数
     * @return {@link ResponseProxy<File>}
     */
    default ResponseProxy downloadPost(String url,Map<String,String> param,File file) throws IOException {
       return this.downloadPost(url,param,null,file,-1);
    }



    /**
     * 下载 post
     * @param url          url
     * @param headers      头
     * @param file  下载到文档
     * @param timeout      超时
     * @param param        参数
     * @return {@link ResponseProxy<File>}
     */
     ResponseProxy downloadPost(String url,Map<String,String> param,Headers headers,File file, int timeout) throws IOException;
}
