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
     * @param responseType 响应类型
     * @return {@link T}
     * @throws IOException exception
     */
    default <T> ResponseProxy<T> get(String urlString, Class<T> responseType) throws IOException {
        return get(urlString,responseType,-1);
    };



    /**
     *  get 请求
     *
     * @param urlString    url字符串
     * @param responseType 响应类型
     * @param timeout 超时毫秒
     * @return {@link T}
     * @throws IOException exception
     */
    default <T> ResponseProxy<T> get(String urlString,Class<T> responseType,int timeout) throws IOException{
        return get(urlString,responseType,null,timeout);
    }




    /**
     *  get 请求
     *
     * @param urlString    url字符串
     * @param responseType 响应类型
     * @param headers 请求头
     * @param timeout 超时毫秒
     * @return {@link T}
     * @throws IOException exception
     */
    <T> ResponseProxy<T> get(String urlString,Class<T> responseType,Headers headers,int timeout) throws IOException;






    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @return {@link T}
     * @throws IOException exception
     */
    default <T> ResponseProxy<T> post(String url, Map<String,String> param,Class<T> responseType) throws IOException{
        return post(url,param,responseType,-1);
    };


    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @param timeout 超时
     * @return {@link T}
     * @throws IOException exception
     */
    default <T> ResponseProxy<T> post(String url, Map<String,String> param,Class<T> responseType, int timeout) throws IOException {
        return post(url,param,null,responseType,timeout);
    }



    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @param timeout 超时
     * @return {@link T}
     * @throws IOException exception
     */
    <T> ResponseProxy<T> post(String url, Map<String,String> param,Headers headers,Class<T> responseType, int timeout) throws IOException;





    /**
     *  post 请求
     *
     * @param url     url
     * @param body 请求参数
     * @return {@link T}
     * @throws IOException exception
     */
    default <T> ResponseProxy<T> post(String url,String body,Class<T> responseType) throws IOException {
        return post(url,body,responseType,-1);
    }


    /**
     *  post 请求
     *
     * @param url     url
     * @param body 请求参数
     * @param timeout 超时
     * @return {@link T}
     * @throws IOException exception
     */
    default <T> ResponseProxy<T> post(String url,String body ,Class<T> responseType,int timeout) throws IOException {
        return post(url,body,null,responseType,timeout);
    }


    /**
     * post 请求
     *
     * @param url          url
     * @param body         请求参数
     * @param headers      请求头
     * @param timeout      超时
     * @param responseType 响应类型
     * @return {@link T}
     * @throws IOException exception
     */
    <T> ResponseProxy<T> post(String url,String body ,Headers headers,Class<T> responseType,int timeout) throws IOException;



    /**
     * 下载 get
     *
     * @param url          url
     * @param file 下载到文件
     * @return {@link ResponseProxy<File>}
     */
    default ResponseProxy<File> downloadGet(String url,File file) throws IOException {
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
     ResponseProxy<File> downloadGet(String url,Headers headers,File file, int timeout) throws IOException;



    /**
     * 下载 post
     * @param url          url
     * @param file  下载到文档
     * @param param        参数
     * @return {@link ResponseProxy<File>}
     */
    default ResponseProxy<File> downloadPost(String url,Map<String,String> param,File file) throws IOException {
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
     ResponseProxy<File> downloadPost(String url,Map<String,String> param,Headers headers,File file, int timeout) throws IOException;
}
