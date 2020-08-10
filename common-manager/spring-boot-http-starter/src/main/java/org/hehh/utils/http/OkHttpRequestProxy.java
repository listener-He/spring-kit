package org.hehh.utils.http;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import okhttp3.*;
import org.hehh.utils.http.config.OkHttpProxyConfigurationParameters;
import org.hehh.utils.http.response.OkHttpResponseProxy;
import org.hehh.utils.http.response.ResponseProxy;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: HeHui
 * @date: 2020-08-10 21:37
 * @description: okHttp请求代理
 */
public class OkHttpRequestProxy implements HttpRequestProxy {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    private  OkHttpClient proxy;


    /**
     * x509信托经理
     *
     * @return {@link X509TrustManager}
     */
    private X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }


    public OkHttpRequestProxy(OkHttpProxyConfigurationParameters parameters) throws Exception {
        TimeUnit unit = TimeUnit.MILLISECONDS;

        X509TrustManager x509TrustManager = x509TrustManager();
        /**
         * 信任任何链接
         */
        SSLContext sslContext = SSLContext.getInstance(parameters.getSsl());
        sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());

        this.proxy = new OkHttpClient.Builder().connectionPool(new ConnectionPool(parameters.getMax(),parameters.getKeepAliveDuration(),unit))
            .callTimeout(parameters.getCallTimeout(),unit)
            .connectTimeout(parameters.getConnectTimeout(),unit)
            .retryOnConnectionFailure(parameters.isRetryOnConnectionFailure())
            .followRedirects(parameters.isFollowRedirects())
            .followSslRedirects(parameters.isFollowSslRedirects())
            .sslSocketFactory(sslContext.getSocketFactory(),x509TrustManager)
            .hostnameVerifier((hostname, session) -> true).build();
    }




    /**
     * 好的http请求代理
     *
     * @param factory          工厂
     * @param x509TrustManager x509信托经理
     * @param pool             池
     */
    public OkHttpRequestProxy(SSLSocketFactory factory, X509TrustManager x509TrustManager,ConnectionPool pool){
        this(new OkHttpClient.Builder().connectionPool(pool).sslSocketFactory(factory,x509TrustManager).build());
    }


    /**
     * 好的http请求代理
     *
     * @param okHttpClient 好的http客户端
     */
    public OkHttpRequestProxy(OkHttpClient okHttpClient){
        Assert.notNull(okHttpClient,"OkHttpClient not null");
        this.proxy = okHttpClient;
    }


    /**
     * get 请求
     *
     * @param urlString url字符串
     * @param headers   请求头
     * @param timeout   超时毫秒
     * @throws IOException exception
     */
    @Override
    public ResponseProxy get(String urlString, Headers headers, int timeout) throws IOException {
        return doGet(urlString,null,headers);
    }

    /**
     * post 请求
     *
     * @param url     url
     * @param param   参数
     * @param headers
     * @param timeout 超时
     * @throws IOException exception
     */
    @Override
    public ResponseProxy post(String url, Map<String, String> param, Headers headers, int timeout) throws IOException {
        return doPostForm(url,param,headers);
    }



    /**
     * post 请求
     *
     * @param url     url
     * @param body    请求参数
     * @param headers 请求头
     * @param timeout 超时
     * @throws IOException exception
     */
    @Override
    public ResponseProxy post(String url, String body, Headers headers, int timeout) throws IOException {
        return doPostJson(url,body,headers);
    }



    /**
     * 下载 get
     *
     * @param url     url
     * @param headers 头
     * @param file    下载到文件
     * @param timeout 超时
     * @return {@link ResponseProxy< File >}
     */
    @Override
    public ResponseProxy downloadGet(String url, Headers headers, File file, int timeout) throws IOException {
        Request.Builder builder = getRequestBuilder(url, headers);
        return execute(builder.get().build(),file);
    }

    /**
     * 下载 post
     *
     * @param url     url
     * @param param   参数
     * @param headers 头
     * @param file    下载到文档
     * @param timeout 超时
     * @return {@link ResponseProxy<File>}
     */
    @Override
    public ResponseProxy downloadPost(String url, Map<String, String> param, Headers headers, File file, int timeout) throws IOException {
        Request.Builder builder = getRequestBuilder(url, headers);
        FormBody.Builder body = new FormBody.Builder();
        if(!CollectionUtils.isEmpty(param)){
            param.forEach((k,v)->{
                body.add(k,v);
            });
        }

        return execute(builder.post(body.build()).build(),file);
    }


    /**
     * get请求构建器
     *
     * @param url     url
     * @param headers 头
     * @return {@link Request.Builder}
     */
    @NotNull
    private Request.Builder getRequestBuilder(String url, Headers headers) {
        Request.Builder builder = new Request.Builder().url(url);
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((k, v) -> {
                builder.addHeader(k, v);
            });
        }
        return builder;
    }


    /**
     *  gei请求
     *
     * @param url     url
     * @param body    身体
     * @param headers 头
     * @return {@link OkHttpResponseProxy}* @throws IOException ioexception
     */
    public OkHttpResponseProxy doGet(String url,String body,Headers headers) throws IOException {
        Request.Builder builder = getRequestBuilder(url, headers);
        if(StrUtil.isNotBlank(body)){
            builder.method("GET",RequestBody.Companion.create(body, JSON));
        }else{
            builder.get();
        }
        return execute(builder.build());
    }



    /**
     * post 请求, 请求数据为 json 的字符串
     * @param url       请求url地址
     * @param json      请求数据, json 字符串
     * @return string
     */
    public OkHttpResponseProxy doPostJson(String url, String json,Headers headers) throws IOException {
        return executePost(url, json, JSON,headers);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     * @param url       请求url地址
     * @param param      请求数据
     * @return string
     */
    public OkHttpResponseProxy doPostForm(String url, Map<String,String> param,Headers headers) throws IOException {
        FormBody.Builder body = new FormBody.Builder();
        if(!CollectionUtils.isEmpty(param)){
            param.forEach((k,v)->{
                body.add(k,v);
            });
        }
        Request.Builder builder = getRequestBuilder(url,headers);
        return execute(builder.post(body.build()).build());
    }



    /**
     * post 请求, 请求数据为 xml 的字符串
     * @param url       请求url地址
     * @param xml       请求数据, xml 字符串
     * @return string
     */
    public OkHttpResponseProxy doPostXml(String url, String xml,Headers headers) throws IOException {
        return executePost(url, xml, XML,headers);
    }



    /**
     * 执行post
     *
     * @param url         url
     * @param data        数据
     * @param contentType 内容类型
     * @return {@link OkHttpResponseProxy}* @throws IOException ioexception
     */
    private OkHttpResponseProxy executePost(String url, String data, MediaType contentType,Headers headers) throws IOException {
        RequestBody requestBody = RequestBody.Companion.create(data, contentType);
        Request.Builder builder = getRequestBuilder(url,headers);
        return execute(builder.post(requestBody).build());
    }



    /**
     * 执行
     *
     * @param request 请求
     * @return {@link OkHttpResponseProxy}* @throws IOException ioexception
     */
    private OkHttpResponseProxy execute(Request request) throws IOException {
        return new OkHttpResponseProxy(proxy.newCall(request).execute());
    }

    /**
     * 执行
     *
     * @param request 请求
     * @return {@link OkHttpResponseProxy}* @throws IOException ioexception
     */
    private OkHttpResponseProxy execute(Request request,File file) throws IOException {
        Response response = proxy.newCall(request).execute();
        if(file.isDirectory()){
            file = FileUtil.file(file, getFileNameFromDisposition(response.headers().toMultimap(), request.url().encodedPath()));
        }
        return new OkHttpResponseProxy(response,file);
    }





    /**
     * 从Content-Disposition头中获取文件名
     *
     * @return 文件名，empty表示无
     */
    private String getFileNameFromDisposition(Map<String, List<String>> headers,String path) {
        String fileName = null;
        String desposition = null;
        List<String> list = headers.get("Content-Disposition");
        if(!CollectionUtils.isEmpty(list)){
            desposition = list.get(0);
        }

        if (StrUtil.isNotBlank(desposition)) {
            fileName = ReUtil.get("filename=\"(.*?)\"", desposition, 1);
            if (StrUtil.isBlank(fileName)) {
                fileName = StrUtil.subAfter(desposition, "filename=", true);
            }
        }

        if(StrUtil.isBlank(fileName)){
            // 从路径中获取文件名
            fileName = StrUtil.subSuf(path, path.lastIndexOf('/') + 1);
            if (StrUtil.isBlank(fileName)) {
                // 编码后的路径做为文件名
                fileName = URLUtil.encodeQuery(path, CharsetUtil.CHARSET_UTF_8);
            }
        }


        return fileName;
    }


//    public static void main(String[] args) throws Exception {
//        HttpRequestProxy proxy = new OkHttpRequestProxy(new OkHttpProxyConfigurationParameters());
//        ResponseProxy get = proxy.downloadGet("https://file.buychemi.cn/c676e14126050de510899993fa4cd66b.png", new File("/Users/hehui/Documents"));
//
//        System.out.println(get);
//    }
}
