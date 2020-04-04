package org.hehh.cloud.spring.decrypt.adapter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.common.utils.StrKit;
import org.hehh.cloud.spring.decrypt.DecryptManager;
import org.hehh.cloud.spring.decrypt.IDecrypt;
import org.hehh.cloud.spring.decrypt.annotation.Decrypt;
import org.hehh.cloud.spring.decrypt.param.DecryptParameter;
import org.hehh.cloud.spring.exception.DecryptException;
import org.hehh.cloud.spring.mvc.copy.ReplaceInputStreamHttpServletRequest;
import org.hehh.cloud.spring.mvc.copy.ReplaceParamHttpServletRequest;
import org.hehh.cloud.spring.mvc.core.CopyNativeWebRequest;
import org.hehh.cloud.spring.mvc.crypto.IDecryptAdapter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2020-03-29 01:34
 * @description: 表单提交 解密切面
 */
@Slf4j
public class RequestFormDecryptAdapter implements IDecryptAdapter {


    /**
     * 解密对象
     */
    private final DecryptManager decryptManager;


    /**
     * 是否值解密模式
     * true：只解密key对应的值，客户端请求还需正常的json格式，只是对value进行加密
     * false: 客户端请求参数是一个被加密后对json格式，服务端对字符串进行解密
     */
    private final boolean valueModel;


    /**
     * 是否需要扫描注解才解密
     */
    private final boolean scanAnnotation;

    /**
     *  被扫描的注解
     */
    private final Class<? extends Decrypt> annotation;




    public RequestFormDecryptAdapter(DecryptManager decryptManager) {
        this.decryptManager = decryptManager;
        valueModel = false;
        this.scanAnnotation = true;
        this.annotation = Decrypt.class;
    }


    public RequestFormDecryptAdapter(DecryptManager decryptManager, DecryptParameter decryptParameter) {
        this.decryptManager = decryptManager;
        this.scanAnnotation = decryptParameter.isScanAnnotation();
        this.valueModel = decryptParameter.isValueModel();
        this.annotation = decryptParameter.getAnnotation();
    }






    /**
     * 是否需要有注解才解密
     *
     * @return
     */
    @Override
    public boolean isScanAnnotation() {
        return scanAnnotation;
    }

    /**
     * 是否支持解密
     *
     * @param parameter 参数
     * @param mediaType 请求内容类型
     * @return
     */
    @Override
    public boolean supportsDecrypt(MethodParameter parameter, MediaType mediaType) {
        return mediaType.includes(MediaType.APPLICATION_FORM_URLENCODED) || mediaType.includes(MediaType.MULTIPART_FORM_DATA);
    }


    /**
     * 解密
     *
     * @param parameter  url绑定方法参数
     * @param request    原始请求
     * @param mediaType  媒体类型
     * @param paramClass 参数类型
     * @return
     */
    @Override
    public NativeWebRequest decode(MethodParameter parameter, NativeWebRequest request, MediaType mediaType, Class<?> paramClass) {

        IDecrypt decrypt = decryptManager.get();
        /**
         *  是否整体解密？,表单提交貌似没有整体解密的，除非是url上面
         */
        if (!valueModel) {
            HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
            String param = servletRequest.getQueryString();
            if(StrUtil.isBlank(param)){
                log.warn("解密表提交失败,提交数据为空");
                return request;
            }
            try {
                String decryptStr = decrypt.decryptStr(param);

                /**
                 *  不支持非json格式
                 */
                if(!StrKit.isJson(decryptStr)){
                    throw new DecryptException("Unsupported non-json format");
                }
                /**
                 *  提交的数据不应该是一个数组
                 */
                if(StrKit.isJsonArray(decryptStr)){
                    throw new DecryptException("Submitted data should be key-value, not an array");
                }

                return new CopyNativeWebRequest(request,new ReplaceParamHttpServletRequest(servletRequest,decryptStr,true));
            }catch (Exception e){
                throw new DecryptException("Decrypted form submission count exception",e);
            }

        }


        /**
         *  无法确认使用的注解，所以只能用参数名去查参数
         */
        String parameterName = parameter.getParameterName();
        String param = request.getParameter(parameterName);
        if(StrUtil.isBlank(param)){
            log.warn("解密表提交失败,参数:{}为空",parameterName);
            return request;
        }

        String decryptStr = decrypt.decryptStr(param);


        if(StrUtil.isNotBlank(decryptStr)){
            Map<String, String[]> parameterMap = request.getParameterMap();
            if(parameterMap != null){
                parameterMap.put(parameterName,new String[]{decryptStr});
            }
            return new CopyNativeWebRequest(request,new ReplaceParamHttpServletRequest(request.getNativeRequest(HttpServletRequest.class),parameterMap,false));
        }

        return request;
    }






    /**
     * 支持的注解
     *
     * @return
     */
    @Override
    public Class<? extends Decrypt> annotation() {
        return annotation;
    }
}
