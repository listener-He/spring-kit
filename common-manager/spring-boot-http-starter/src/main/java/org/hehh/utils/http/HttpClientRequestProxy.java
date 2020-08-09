package org.hehh.utils.http;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.utils.http.config.HttpClientProxyConfigurationParameters;
import org.hehh.utils.http.response.HttpClientResponseProxy;
import org.hehh.utils.http.response.ResponseProxy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-08-09 17:18
 * @description: http-client 请求代理
 */
public class HttpClientRequestProxy  implements HttpRequestProxy, InitializingBean {


    private HttpClient client;


    private final PoolingHttpClientConnectionManager manager;
    private final RequestConfig config;
    private final HttpRequestRetryHandler retryHandler;



    /**
     * http客户机请求代理
     */
    public HttpClientRequestProxy(){
        this(new HttpClientProxyConfigurationParameters());
    }

    /**
     * http客户机请求代理
     *
     * @param parameters 参数
     */
    public HttpClientRequestProxy(HttpClientProxyConfigurationParameters parameters){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(parameters.getMax());
        manager.setDefaultMaxPerRoute(parameters.getDefaultMaxPerRoute());
        manager.setValidateAfterInactivity(parameters.getValidateAfterInactivity());


        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(parameters.getConnectionRequestTimeout())
            .setConnectTimeout(parameters.getConnectTimeout())
            .setSocketTimeout(parameters.getSocketTimeout())
           .build();

         HttpRequestRetryHandler retryHandler = null;
         if(parameters.isRetryEnabled()){
            retryHandler =  new DefaultHttpRequestRetryHandler(parameters.getRetryCount(),parameters.isRetryEnabled());
         }


         this.manager = manager;
         this.config = config;
         this.retryHandler = retryHandler;
    }


    /**
     * http客户机请求代理
     *
     * @param manager      经理
     * @param config       配置
     * @param retryHandler 重试处理程序
     */
    public HttpClientRequestProxy(PoolingHttpClientConnectionManager manager,RequestConfig config,HttpRequestRetryHandler retryHandler){
        this.manager = manager;
        this.config = config;
        this.retryHandler = retryHandler;
    }



    /**
     * 在属性设置
     *
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.client = createHttpClientBuilder(manager,config,retryHandler).build();
    }




    /**
     * 创建http客户端构建器
     *
     * @param manager      经理
     * @param config       配置
     * @param retryHandler 重试处理程序
     * @return {@link HttpClientBuilder}
     */
    public static HttpClientBuilder createHttpClientBuilder(PoolingHttpClientConnectionManager manager, RequestConfig config, HttpRequestRetryHandler retryHandler) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        /**
         * 设置连接池
         */
        httpClientBuilder.setConnectionManager(manager);
        /**
         * 设置超时时间
         */
        httpClientBuilder.setDefaultRequestConfig(config);

        /**
         * 定义连接管理器将由多个客户端实例共享。如果连接管理器是共享的，则其生命周期应由调用者管理，如果客户端关闭则不会关闭。
         */
        httpClientBuilder.setConnectionManagerShared(true);

        /**
         * Keep-alive：timeout=5，max=100
         * 意思是说：过期时间5秒，max是最多100次请求，强制断掉连接，也就是在timeout时间内每来一个新的请求，max会自动减1,直到为0，强制断掉连接
         */
        ConnectionKeepAliveStrategy myStrategy = new DefaultConnectionKeepAliveStrategy();
        httpClientBuilder.setKeepAliveStrategy(myStrategy);

        /**
         *  设置重试策略
         */
        httpClientBuilder.setRetryHandler(retryHandler);

        /**
         *  关闭重试
         */
        if(retryHandler == null){
            httpClientBuilder.disableAutomaticRetries();
        }
        return httpClientBuilder;
    }





    /**
     * get 请求
     *
     * @param urlString    url字符串
     * @param responseType 响应类型
     * @param headers      请求头
     * @param timeout      超时毫秒
     * @return {@link T}
     */
    @Override
    public <T> ResponseProxy<T> get(String urlString, Class<T> responseType, Headers headers, int timeout) throws IOException {
        HttpGet get = new HttpGet(urlString);
        if(!CollectionUtils.isEmpty(headers)){
            headers.forEach((k,v)->{
                get.setHeader(k,v);
            });
        }

        if(timeout > 0){
             get.setConfig(RequestConfig.copy(this.config).setSocketTimeout(timeout).build());
        } else if(timeout == -1){
            get.setConfig(config);
        }


        return new HttpClientResponseProxy(client.execute(get),responseType);
    }



    /**
     * post 请求
     *
     * @param url          url
     * @param param        参数
     * @param headers
     * @param responseType
     * @param timeout      超时
     * @return {@link T}
     */
    @Override
    public <T> ResponseProxy<T> post(String url, Map<String, String> param, Headers headers, Class<T> responseType, int timeout) throws IOException {
        HttpPost post = new HttpPost(url);
        if(!CollectionUtils.isEmpty(headers)){
            headers.forEach((k,v)->{
                post.setHeader(k,v);
            });
        }

        if(timeout > 0){
            post.setConfig(RequestConfig.copy(this.config).setSocketTimeout(timeout).build());
        } else if(timeout == -1){
            post.setConfig(config);
        }

        if(!CollectionUtils.isEmpty(param)){
             post.setEntity(new UrlEncodedFormEntity(NameValuePairBuild.processor(param), Charset.defaultCharset()));
        }

        return new HttpClientResponseProxy(client.execute(post),responseType);
    }



    /**
     * post 请求
     *
     * @param url          url
     * @param body         请求参数
     * @param headers      请求头
     * @param responseType
     * @param timeout      超时
     * @return {@link T}
     */
    @Override
    public <T> ResponseProxy<T> post(String url, String body, Headers headers, Class<T> responseType, int timeout) throws IOException {
        HttpPost post = new HttpPost(url);
        if(!CollectionUtils.isEmpty(headers)){
            headers.forEach((k,v)->{
                post.setHeader(k,v);
            });
        }

        if(timeout > 0){
            post.setConfig(RequestConfig.copy(this.config).setSocketTimeout(timeout).build());
        } else if(timeout == -1){
            post.setConfig(config);
        }

        if(StringUtils.hasText(body)){
            post.setEntity(new StringEntity(body, Charset.defaultCharset()));
        }

        return new HttpClientResponseProxy(client.execute(post),responseType);
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
    public ResponseProxy<File> downloadGet(String url, Headers headers, File file, int timeout) throws IOException {
        HttpGet get = new HttpGet(url);
        if(!CollectionUtils.isEmpty(headers)){
            headers.forEach((k,v)->{
                get.setHeader(k,v);
            });
        }

        if(timeout > 0){
            get.setConfig(RequestConfig.copy(this.config).setSocketTimeout(timeout).build());
        } else if(timeout == -1){
            get.setConfig(config);
        }
        HttpResponse execute = client.execute(get);
        return download(file, execute,url);
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
    public ResponseProxy<File> downloadPost(String url, Map<String, String> param, Headers headers, File file, int timeout) throws IOException {
        HttpPost post = new HttpPost(url);
        if(!CollectionUtils.isEmpty(headers)){
            headers.forEach((k,v)->{
                post.setHeader(k,v);
            });
        }

        if(timeout > 0){
            post.setConfig(RequestConfig.copy(this.config).setSocketTimeout(timeout).build());
        } else if(timeout == -1){
            post.setConfig(config);
        }

        if(!CollectionUtils.isEmpty(param)){
            post.setEntity(new UrlEncodedFormEntity(NameValuePairBuild.processor(param), Charset.defaultCharset()));
        }
        HttpResponse execute = client.execute(post);
        return download(file, execute,url);

    }




    /**
     * 下载
     *
     * @param file    文件
     * @param execute 执行
     * @return {@link ResponseProxy<File>}* @throws IOException ioexception
     */
    private ResponseProxy<File> download(File file, HttpResponse execute,String path) throws IOException {
        HttpEntity entity = execute.getEntity();

        HttpClientResponseProxy<File> responseProxy = new HttpClientResponseProxy<>(execute, File.class);

        if (file.isDirectory()) {
            file = FileUtil.file(file, getFileNameFromDisposition(responseProxy.getHeaders(),path));
        }

        responseProxy.setData(FileUtil.writeFromStream(entity.getContent(), file));

        return responseProxy;
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
//        HttpClientRequestProxy proxy = new HttpClientRequestProxy();
//        proxy.afterPropertiesSet();
//
//        ResponseProxy<ResultA> responseProxy = proxy.get("https://www.buychemi.cn/qiye/d49414/chanpin/p2?keyword=",ResultA.class );
//        ResultA resultA = responseProxy.getData();
//
//        System.out.println(responseProxy);
//    }
//
//
//    static class ResultA extends Result<List<A>>{
//
//    }
//
//
//    @Data
//    static class A {
//
//
//        private int id;
//        private int enterpriseId;
//        private String name;
//        private int isMain;
//    }

}
