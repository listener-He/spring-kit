package org.hehh.weChat;

import org.hehh.utils.http.HttpRequest;
import org.hehh.weChat.req.WxRequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-08-06 19:50
 * @description: 请求授权实现
 */
public class RequestAuthImpl implements RequestAuth {


    private final APIAccessTokenRequest request;

    private final Map<String,String> appMap;




    /**
     * 请求身份验证impl
     *
     * @param httpProxy    http代理
     * @param tokenStorage 令牌存储
     * @param apps         应用程序
     */
    public RequestAuthImpl(HttpRequest httpProxy, AuthStorage tokenStorage, List<WxConfigurationParameter> apps) {
        this.request = new APIAccessTokenRequest(httpProxy,tokenStorage);
        assert apps != null : "确少微信应用配置参数";

        Map<String, String> map = apps.stream().collect(Collectors.toMap(WxConfigurationParameter::getAppId, WxConfigurationParameter::getAppSecret));
        appMap = Collections.unmodifiableMap(map);
    }


    /**
     * 获得令牌
     *
     * @param param 参数
     * @return {@link Optional <String>}
     */
    @Override
    public Optional<String> getToken(WxRequestParam param) {
        return Optional.ofNullable(request.getAccessToken(param.getAppId(),appMap.get(param.getAppId())));
    }
}
