package cn.hutool.http;

import cn.hutool.json.JSONUtil;
import org.hehh.utils.http.response.ResponseProxy;

import java.io.IOException;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-09 19:04
 * @description: hutool响应代理
 */
public class HuToolResponseProxy  implements ResponseProxy {


    private Object data;

    private String body;

    private final HttpResponse proxy;

    /**
     * 构造
     *
     * @param httpConnection {@link HttpConnection}
     * @param charset        编码，从请求编码中获取默认编码
     * @param isAsync        是否异步
     * @param isIgnoreBody   是否忽略读取响应体
     * @since 3.1.2
     */
    public HuToolResponseProxy(HttpConnection httpConnection, Charset charset, boolean isAsync, boolean isIgnoreBody) {
        this(new HttpResponse(httpConnection, charset, isAsync, isIgnoreBody));

    }


    /**
     * 胡工具响应代理
     *
     * @param response     响应
     */
    public HuToolResponseProxy(HttpResponse response){
        proxy = response;
    }


    public void setData(Object data) {
        this.data = data;
    }




    /**
     * 获得地位
     *
     * @return int
     */
    @Override
    public int getStatus() {
        return proxy.getStatus();
    }

    /**
     * 获取响应 cookie
     *
     * @return {@link List< HttpCookie >}
     */
    @Override
    public List<HttpCookie> getCookies() {
        return proxy.getCookies();
    }

    /**
     * 获取数据
     *
     * @return {@link T}
     */
    @Override
    public <T> T getData(Class<T> tClass) throws IOException {
        if(this.data == null){
            if(isOk()){
                this.data = JSONUtil.toBean(body(),tClass);
            }
        }
        return (T) this.data;
    }

    /**
     * 主体数据 json/body
     */
    @Override
    public String body() throws IOException {
        if(this.body == null){
            this.body = proxy.body();
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
//            this.data = (T) JSONUtil.toList(JSONUtil.parseArray(proxy.body()), elementClass);
//        }
//
//        return (List<E>)this.data;
//    }

    /**
     * 得到响应头
     *
     * @return {@link Map <String,  List <String>>}
     */
    @Override
    public Map<String, List<String>> getHeaders() {
        return proxy.headers;
    }
}
