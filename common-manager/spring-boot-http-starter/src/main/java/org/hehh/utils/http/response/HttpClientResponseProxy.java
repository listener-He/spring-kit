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
public class HttpClientResponseProxy<T> implements ResponseProxy<T> {


    private final HttpResponse proxy;

    private Class<T> responseType;

    private T data;

    private Map<String, List<String>> headers;


    public HttpClientResponseProxy(HttpResponse proxy,Class<T> responseType) {
        this.proxy = proxy;
        this.responseType = responseType;
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


    public void setData(T data) {
        this.data = data;
    }




    /**
     * 获取数据
     *
     * @return {@link T}
     */
    @Override
    public T getData() throws IOException {
        if(this.data == null){
            if(List.class.isAssignableFrom(responseType)){
                getData(Object.class);
            }else{
                HttpEntity entity = proxy.getEntity();
                try {
                    String value = EntityUtils.toString(entity, Charset.defaultCharset());
                    if(String.class.equals(responseType)){
                        this.data = (T)value;
                    }else if(Number.class.isAssignableFrom(responseType)){
                        this.data = (T) NumberUtils.parseNumber(value,(Class<? extends Number>) responseType);
                    }else{
                        this.data = JSONUtil.toBean(value,responseType);
                    }
                }finally {
                    EntityUtils.consume(entity);
                }
            }

        }
         return this.data;
    }

    /**
     * 获取数据
     *
     * @param elementClass 元素类
     * @return {@link List<E>}* @throws IOException ioexception
     */
    @Override
    public <E> List<E> getData(Class<E> elementClass) throws IOException {
        if(!List.class.isAssignableFrom(responseType)){
            return null;
        }

        if(this.data == null){
            HttpEntity entity = proxy.getEntity();
            try {
                String value = EntityUtils.toString(entity, Charset.defaultCharset());
                this.data = (T) JSONUtil.toList(JSONUtil.parseArray(value), elementClass);
            }finally {
                EntityUtils.consume(entity);
            }

        }
        return (List<E>)this.data;
    }

    /**
     * 得到响应头
     *
     * @return {@link Map <String,  List <String>>}
     */
    @Override
    public Map<String, List<String>> getHeaders() {

        if(this.headers == null){
            Header[] headers = proxy.getAllHeaders();
            if(headers != null){
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
