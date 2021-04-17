package org.hehh.utils.http.response;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-10 21:48
 * @description: okHttp 响应代理
 */
public class OkHttpResponseProxy  implements ResponseProxy{


    private final int status;

    private final Map<String, List<String>> headers;

    private Object data;

    private String body;



    /**
     * 好的http响应代理
     *
     * @param response 响应
     */
    public OkHttpResponseProxy(Response response) {
        this.status = response.code();
        this.headers  = response.headers().toMultimap();
        if (response.isSuccessful()) {
            try {
                this.body =  response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                response.close();
            }
        }
    }


    /**
     * 好的http响应代理
     *
     * @param response 响应
     * @param file     文件
     */
    public OkHttpResponseProxy(Response response, File file) {
        this.status = response.code();
        this.headers  = response.headers().toMultimap();
        if (response.isSuccessful()) {
            try {
                 FileUtil.writeFromStream(response.body().byteStream(),file);
                 this.data = file;
                 this.body = "";
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                response.close();
            }
        }
    }


    /**
     * 获得地位
     *
     * @return int
     */
    @Override
    public int getStatus() {
        return status;
    }



    /**
     * 获取数据 调用body方法然后json转换
     *
     * @param tClass
     * @return {@link T}
     * @see ResponseProxy#body()
     */
    @Override
    public <T> T getData(Class<T> tClass) throws IOException {
        if(this.data == null && StrUtil.isNotBlank(body)){
            this.data = JSONUtil.toBean(body,tClass);
        }

        return (T) this.data;
    }



    /**
     * 主体数据 json/body
     */
    @Override
    public String body() throws IOException {
        return body;
    }

    /**
     * 得到响应头
     *
     * @return {@link Map <String,  List <String>>}
     */
    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
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
