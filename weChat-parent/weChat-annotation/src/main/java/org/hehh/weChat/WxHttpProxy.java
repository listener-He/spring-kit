package org.hehh.weChat;

import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-06 17:37
 * @description: 微信请求代理
 */
public interface WxHttpProxy {


    /**
     *  get 请求
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
    <T> T get(String urlString,Class<T> responseType,int timeout);



    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @return {@link T}
     */
    default <T> T post(String url, Map<String,Object> param){
        return post(url,param,0);
    };



    /**
     *  post 请求
     *
     * @param url     url
     * @param param   参数
     * @param timeout 超时
     * @return {@link T}
     */
    <T> T post(String url, Map<String,Object> param,int timeout);




    /**
     *  post 请求
     *
     * @param url     url
     * @param body 请求参数
     * @param timeout 超时
     * @return {@link T}
     */
    <T> T post(String url,String body ,int timeout);


}
