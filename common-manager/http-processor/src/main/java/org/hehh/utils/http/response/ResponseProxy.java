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
public interface ResponseProxy<T> {


    /**
     * 获得地位
     *
     * @return int
     */
    int getStatus();


    /**
     * 获取数据
     *
     * @return {@link T}
     */
    T getData() throws IOException;



    /**
     * 获取数据 纯属方便范形集合调用
     *
     * @param elementClass 元素类
     * @return {@link List<E>}* @throws IOException ioexception
     */
    <E> List<E> getData(Class<E> elementClass) throws IOException;


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
