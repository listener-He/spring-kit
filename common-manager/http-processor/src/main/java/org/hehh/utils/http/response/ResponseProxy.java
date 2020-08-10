package org.hehh.utils.http.response;


import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-09 18:53
 * @description: 响应代理
 */
public interface ResponseProxy {




    /**
     * 获得地位
     *
     * @return int
     */
    int getStatus();


    /**
     * 获取数据 调用body方法然后json转换
     * @see ResponseProxy#body()
     * @param tClass
     * @return {@link T}
     */
    <T> T getData(Class<T> tClass) throws IOException;

    /**
     * 主体数据 json/body
     *
     */
    String body() throws IOException;



    /**
     * 得到响应头
     *
     * @return {@link Map<String, List<String>>}
     */
    Map<String, List<String>> getHeaders();


    /**
     *  获取响应 cookie
     *
     * @return {@link List<HttpCookie>}
     */
    List<HttpCookie> getCookies();




    /**
     * 请求是否成功，判断依据为：状态码范围在200~299内。
     *
     * @return 是否成功请求
     */
     default boolean isOk() {
        return this.getStatus() >= 200 && this.getStatus() < 300;
    }
}
