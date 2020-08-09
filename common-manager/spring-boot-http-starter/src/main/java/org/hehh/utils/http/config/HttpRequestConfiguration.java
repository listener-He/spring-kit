package org.hehh.utils.http.config;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.OS;
import org.apache.http.client.HttpClient;
import org.hehh.utils.http.HttpClientRequestProxy;
import org.hehh.utils.http.HttpRequestProxy;
import org.hehh.utils.http.HuToolHttpRequestProxy;
import org.hehh.utils.http.userAgent.HuToolUserAgentProcessor;
import org.hehh.utils.http.userAgent.UserAgentConfiguration;
import org.hehh.utils.http.userAgent.UserAgentProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-08-09 23:43
 * @description: http 代理配置
 */
@Configuration
public class HttpRequestConfiguration {


    /**
     * hutool工具http代理配置
     *
     * @author hehui
     * @date 2020/08/09
     */
    @Configuration
    @ConditionalOnClass(HttpUtil.class)
    static class HuToolHttpProxyConfiguration {


        /**
         * 用户代理处理器
         *
         * @param configurations 配置
         * @return {@link UserAgentProcessor}
         */
        @Bean
        @ConditionalOnMissingBean(UserAgentProcessor.class)
        public UserAgentProcessor userAgentProcessor(@Autowired(required = false) List<UserAgentConfiguration> configurations){
            return new HuToolUserAgentProcessor(configurations);
        }



        /**
         * hutool工具http请求代理
         *
         * @return {@link HttpRequestProxy}
         */
        @Bean
        @ConditionalOnMissingBean(HttpRequestProxy.class)
        public HttpRequestProxy huToolHttpRequestProxy() {
            return new HuToolHttpRequestProxy();
        }
    }


    /**
     * http客户端代理配置
     *
     * @author hehui
     * @date 2020/08/09
     */
    @Configuration
    @ConditionalOnClass(HttpClient.class)
    static class HttpClientProxyConfiguration {

        /**
         * 配置参数
         *
         * @return
         */
        @Bean
        @ConfigurationProperties("spring.http.proxy")
        public HttpClientProxyConfigurationParameters httpClientProxyConfigurationParameters() {
            return new HttpClientProxyConfigurationParameters();
        }


        /**
         * 请求配置
         *
         * @param proxyConfigurationParameters
         * @return
         */
        @Bean
        @ConditionalOnMissingBean(HttpRequestProxy.class)
        public HttpRequestProxy httpClientRequestProxy(HttpClientProxyConfigurationParameters proxyConfigurationParameters) {
            return new HttpClientRequestProxy(proxyConfigurationParameters);
        }
    }
}
