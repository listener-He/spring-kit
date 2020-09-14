package org.hehh.cloud.spring.mvc.sign;

import cn.hutool.core.io.IoUtil;
import org.hehh.cloud.spring.exception.SignException;
import org.hehh.cloud.spring.mvc.request.ReplaceInputStreamHttpServletRequest;
import org.hehh.utils.StrKit;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Map;

/**
 *  写过头了，后来发现可以不用分开
 * @author: HeHui
 * @date: 2020-09-14 23:47
 * @description: body签名切面
 */
@Deprecated
public class SignBodyAdapter extends SignAdapter {


    /**
     * 信号适配器
     *
     * @param signVerify    签名验证
     * @param signName      签的名字
     * @param timeName      时间的名字
     * @param overdueTime   过期时间
     * @param signSecretKey 签名密钥
     * @param appName       应用程序名称
     */
    public SignBodyAdapter(SignVerify signVerify,SignSecretKey signSecretKey, String signName, String timeName, String appName, long overdueTime) {
        super(signVerify, signSecretKey, signName, timeName, appName, overdueTime);
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
    public boolean supports(HandlerMethod handlerMethod, MediaType mediaType) {
        return mediaType != null && mediaType.includes(MediaType.APPLICATION_JSON);
    }

    /**
     * 验证
     *
     * @param signature     签名注解 use {@link Controller} Class or Method
     * @param signRequest   标志要求 客户端请求的签名参数
     * @param request       请求
     * @param handlerMethod 处理程序方法
     * @return {@link HttpServletRequest} 如果读取了body后需要把body重新放进去. example {@link ReplaceInputStreamHttpServletRequest}
     */
    @Override
    protected HttpServletRequest verify(Signature signature, SignRequest signRequest, HttpServletRequest request, HandlerMethod handlerMethod) {

        try {
            String body = IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, String[]> paramMap = request.getParameterMap();

            if(StringUtils.hasText(body) || !CollectionUtils.isEmpty(paramMap)){
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
