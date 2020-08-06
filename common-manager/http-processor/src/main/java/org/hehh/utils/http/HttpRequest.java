package org.hehh.utils.http;

import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-06 18:58
 * @description: http 请求
 */
public interface HttpRequest {


    /**  get 请求
     *
     * @param urlString    url字符串
     * @param responseType 响应类型
     * @return {@link T}
     */
    default <T> T get(String urlString,Class<T> responseType){
        return get(urlString,responseType,0);
    };



    /**
     *  get 请求
     *
     * @param urlString    url字符串
     * @param responseType 响应类型
     * @param timeout 超时毫秒
     * @return {@link T}
     */
    default <T> T get(String urlString,Class<T> responseType,int timeout){
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
     */
    <T> T get(String urlString,Class<T> responseType,Headers headers,int timeout);






    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @return {@link T}
     */
    default <T> T post(String url, Map<String,Object> param,Class<T> responseType){
        return post(url,param,responseType,0);
    };


    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @param timeout 超时
     * @return {@link T}
     */
    default <T> T post(String url, Map<String,Object> param,Class<T> responseType, int timeout){
        return post(url,param,null,responseType,timeout);
    }



    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @param timeout 超时
     * @return {@link T}
     */
    <T> T post(String url, Map<String,Object> param,Headers headers,Class<T> responseType, int timeout);





    /**
     *  post 请求
     *
     * @param url     url
     * @param body 请求参数
     * @return {@link T}
     */
    default <T> T post(String url,String body,Class<T> responseType){
        return post(url,body,responseType,0);
    }


    /**
     *  post 请求
     *
     * @param url     url
     * @param body 请求参数
     * @param timeout 超时
     * @return {@link T}
     */
    default <T> T post(String url,String body ,Class<T> responseType,int timeout){
        return post(url,body,null,responseType,timeout);
    }


    /**
     *  post 请求
     *
     * @param url     url
     * @param body 请求参数
     * @param headers 请求头
     * @param timeout 超时
     * @return {@link T}
     */
    <T> T post(String url,String body ,Headers headers,Class<T> responseType,int timeout);
}
