package org.hehh.utils.http.response;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.util.NumberUtils;

import java.io.IOException;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-09 19:43
 * @description: http client 响应代理
 */
public class HttpClientResponseProxy implements ResponseProxy {


    private final HttpResponse proxy;


    private Object data;

    private String body;

    private Map<String, List<String>> headers;


    public HttpClientResponseProxy(HttpResponse proxy) {
        this.proxy = proxy;
    }


    /**
     * 获得地位
     *
     * @return int
     */
    @Override
    public int getStatus() {
        return proxy.getStatusLine().getStatusCode();
    }


    public void setData(Object data) {
        this.data = data;
    }


    /**
     * 获取数据
     */
    @Override
    public <T> T getData(Class<T> responseType) throws IOException {
        if (this.data == null) {

            String value = body();
            if (String.class.equals(responseType)) {
                this.data = (T) value;
            } else if (Number.class.isAssignableFrom(responseType)) {
                this.data = (T) NumberUtils.parseNumber(value, (Class<? extends Number>) responseType);
            } else {
                this.data = JSONUtil.toBean(value, responseType);
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
            HttpEntity entity = proxy.getEntity();
            try {
                this.body = EntityUtils.toString(entity, Charset.defaultCharset());
            } finally {
                EntityUtils.consume(entity);
            }
        }

        return this.body;
    }

    //    /**
//     * 获取数据
//     *
//     * @param elementClass 元素类
//     * @return {@link List<E>}* @throws IOException ioexception
//     */
//    @Override
//    public <E> List<E> getData(Class<E> elementClass) throws IOException {
//        if(!List.class.isAssignableFrom(responseType)){
//            return null;
//        }
//
//        if(this.data == null){
//            HttpEntity entity = proxy.getEntity();
//            try {
//                String value = EntityUtils.toString(entity, Charset.defaultCharset());
//                this.data = (T) JSONUtil.toList(JSONUtil.parseArray(value), elementClass);
//            }finally {
//                EntityUtils.consume(entity);
//            }
//
//        }
//        return (List<E>)this.data;
//    }

    /**
     * 得到响应头
     *
     * @return {@link Map <String,  List <String>>}
     */
    @Override
    public Map<String, List<String>> getHeaders() {

        if (this.headers == null) {
            Header[] headers = proxy.getAllHeaders();
            if (headers != null) {
                this.headers = new HashMap<>(headers.length);
                for (Header header : headers) {
                    this.headers.put(header.getName(), Arrays.asList(header.getValue()));
                }
            }
        }
        return this.headers;
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
}
