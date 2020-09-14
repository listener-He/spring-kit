package org.hehh.cloud.spring.mvc.sign;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.spring.exception.SignException;
import org.hehh.cloud.spring.mvc.request.ReplaceInputStreamHttpServletRequest;
import org.hehh.cloud.spring.mvc.request.method.IHandlerMethodAdapter;
import org.hehh.utils.StrKit;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  从抽象改为不抽象，嗯... 先这样试试看吧
 * @author: HeHui
 * @date: 2020-09-14 22:25
 * @description: 签名适配器
 */
@Slf4j
public  class SignAdapter implements IHandlerMethodAdapter {


    /**
     * 签名验证
     */
    protected final SignVerify signVerify;
    /**
     * 签名密钥
     */
    private final SignSecretKey signSecretKey;

    /**
     * 签名的名字
     */
    public final String signName;

    /**
     * 时间撮的名字
     */
    public final String timeName;

    /**
     * 应用id参数名称
     */
    public final String appName;

    /**
     * 过期时间
     */
    public final long overdueTime;


    /**
     * 信号适配器
     *  @param signVerify  签名验证
     * @param signSecretKey
     * @param signName    签的名字
     * @param timeName    时间的名字
     * @param appName
     * @param overdueTime 过期时间
     */
    public SignAdapter(SignVerify signVerify, SignSecretKey signSecretKey, String signName, String timeName, String appName, long overdueTime) {

        assert signVerify != null : "SignVerify not null!";
        assert signSecretKey != null : "SignSecretKey not null!";
        Assert.hasText(signName, "Sign name not null");
        Assert.hasText(timeName, "Timestamp name not null");
        Assert.hasText(appName, "APP-ID name not null");
        assert overdueTime > 10 : "Timestamp overdue time Minimum of 10";

        this.signVerify = signVerify;
        this.signName = signName;
        this.timeName = timeName;
        this.overdueTime = overdueTime;
        this.signSecretKey = signSecretKey;
        this.appName = appName;
    }


    /**
     * 判断是否支持适配
     *
     * @param handlerMethod the method  to check
     * @param mediaType     request  mediaType
     * @return {@code true} if this resolver supports the supplied method;
     * {@code false} otherwise
     */
    @Override
    public boolean supportsMethod(HandlerMethod handlerMethod, MediaType mediaType) {
        return handlerMethod.hasMethodAnnotation(Signature.class) && supports(handlerMethod, mediaType);
    }


    /**
     * 判断是否支持适配
     *
     * @param handlerMethod the method  to check
     * @param mediaType     request  mediaType
     * @return {@code true} if this resolver supports the supplied method;
     * {@code false} otherwise
     */
    public  boolean supports(HandlerMethod handlerMethod, MediaType mediaType){
        return true;
    }



    /**
     * 在参数绑定之前处理
     *
     * @param request       原始请求
     * @param handlerMethod 适配方法
     * @return {@link HttpServletRequest}
     */
    @Override
    public HttpServletRequest beforeResolver(HttpServletRequest request, HandlerMethod handlerMethod) {
        Signature signature = handlerMethod.getMethodAnnotation(Signature.class);
        assert signature != null : "Illegal call";
        SignRequest signRequest = verifyHeader(request, signature);
        if (signRequest == null) {
            throw new SignException("请求头非法");
        }


        //TODO url参数是否需要签名 ？

        /**    String[] paths = null;
         ServletWebRequest webRequest = new ServletWebRequest(request, null);
         Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
         HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
         if (!CollectionUtils.isEmpty(uriTemplateVars)) {
         paths = uriTemplateVars.values().toArray(new String[]{});
         }*/



        return verify(signature, signRequest, request, handlerMethod);
    }


    /**
     * 验证请求头参数
     *
     * @param request   请求
     * @param signature 签名
     * @return boolean
     */
    public SignRequest verifyHeader(HttpServletRequest request, Signature signature) {
        String appId = request.getHeader(appName);
        if (StringUtils.isEmpty(appId)) {
            log.error("APPId is null");
            return null;
        }
        /**
         *  查询密钥
         */
        String secret = signSecretKey.getSecret(appId);
        if(StringUtils.isEmpty(secret)){
            log.error("Illegal APP-ID");
            return null;
        }
        String sign = request.getHeader(signName);
        if (StringUtils.isEmpty(sign)) {
            log.error("Sign is null");
            return null;
        }

        String timestamp = request.getHeader(timeName);
        if (StringUtils.isEmpty(timestamp) && NumberUtil.isLong(timestamp)) {
            log.error("Timestamp is null or not to Long");
            return null;
        }

        if ((System.currentTimeMillis() - NumberUtils.parseNumber(timestamp, Long.class)) > (signature.overdue() > 0 ? signature.overdue() : overdueTime)) {
            log.error("Timestamp overdue");
            return null;
        }

        SignRequest signRequest = new SignRequest();
        signRequest.setAppId(appId);
        signRequest.setSecret(secret);
        signRequest.setSign(sign);
        signRequest.setTimestamp(NumberUtils.parseNumber(timestamp, Long.class));

        return signRequest;
    }

    /**
     * 得到查询地图
     *
     * @param request 请求
     * @return {@link Map<String, String>}
     */
    protected Map<String, String> getQueryMap(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (StringUtils.hasText(queryString)) {
            Map<String, String> map = new HashMap<>();
            String[] params = queryString.split("&");
            for (int i = 0; i < params.length; i++) {
                String[] p = params[i].split("=");
                if (p.length == 2) {
                    map.put(p[0], p[1]);
                }
            }
            return map;
        }
        return null;
    }




    /**
     * 验证
     *
     * @param signature     签名注解 use {@link org.springframework.stereotype.Controller} Class or Method
     * @param signRequest   标志要求 客户端请求的签名参数
     * @param request       请求
     * @param handlerMethod 处理程序方法
     * @return {@link HttpServletRequest} 如果读取了body后需要把body重新放进去. example {@link ReplaceInputStreamHttpServletRequest}
     */
    protected  HttpServletRequest verify(Signature signature, SignRequest signRequest, HttpServletRequest request, HandlerMethod handlerMethod){
        try {
            String body = IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, String[]> paramMap = request.getParameterMap();

            if(StringUtils.hasText(body) || !CollectionUtils.isEmpty(paramMap)){
                /**
                 *  过滤忽略参数
                 */
                if(paramMap != null && ArrayUtil.isNotEmpty(signature.ignore())){
                    List<String> ignore = Arrays.stream(signature.ignore()).collect(Collectors.toList());
                    paramMap = paramMap.entrySet().stream().filter(v-> !ignore.contains(v.getKey()) ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                }
                if(!signVerify.verifyForm(body,paramMap,signRequest.getSecret(),signRequest.getSign(),signRequest.getTimestamp())){
                    throw new SignException("签名不通过!");
                }

                if(StringUtils.hasText(body)){
                    return new ReplaceInputStreamHttpServletRequest(request, StrKit.hex2Byte(body));
                }
            }
            return request;
        } catch (IOException e) {
            throw new SignException("读取body参数异常",e);
        } catch (SignatureException e) {
            throw new SignException("签名失败",e);
        }
    }
}
