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
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import sun.misc.SoftCache;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-03-29 01:34
 * @description: 表单提交 解密切面
 */
@Slf4j
public class RequestFormDecryptAdapter extends IDecryptAdapter {







    public RequestFormDecryptAdapter(DecryptManager decryptManager) {
        super(decryptManager, false, true, Decrypt.class);
    }


    public RequestFormDecryptAdapter(DecryptManager decryptManager, DecryptParameter decryptParameter) {
        super(decryptManager, decryptParameter.isScanAnnotation(), decryptParameter.isValueModel(), decryptParameter.getAnnotation());
    }








    /**
     * 是否支持解密
     *
     * @param mediaType 请求内容类型
     * @return
     */
    @Override
    protected boolean supportsDecrypt(MediaType mediaType) {
        return mediaType != null && mediaType.includes(MediaType.APPLICATION_FORM_URLENCODED) || mediaType.includes(MediaType.MULTIPART_FORM_DATA);
    }


    /**
     * 解密
     *
     * @param decrypt       解密器
     * @param request       原始请求
     * @param handlerMethod 绑定方法
     * @return
     */
    @Override
    protected HttpServletRequest decode(IDecrypt decrypt, HttpServletRequest request, HandlerMethod handlerMethod) {
        /**
         *  是否整体解密？,表单提交貌似没有整体解密的，除非是url上面
         */
        if (!super.isValueModel()) {
            String param = request.getQueryString();
            if(StrUtil.isBlank(param)){
                log.warn("解密表提交失败,提交数据为空");
                return request;
            }

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

            return new ReplaceParamHttpServletRequest(request,decryptStr,true);


        }


        Map<String, String[]> parameterMap = request.getParameterMap();
        if(parameterMap != null){
            parameterMap = parameterMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
            Decrypt annotation = parameter.getParameterAnnotation(super.getAnnotation());

            if(annotation != null){
                String k = StringUtils.hasText(annotation.value()) ? annotation.value() : parameter.getParameterName();
                String[] v = parameterMap.get(k);


                if(null != v){
                    for (int i = 0; i < v.length; i++) {
                        if(StringUtils.hasText(v[i])){
                            v[i] = decrypt.decryptStr(v[i]);
                        }

                    }
                  parameterMap.put(k,v);
                }
            }
        }

      return   new ReplaceParamHttpServletRequest(request,parameterMap,false);
    }



//    /**
//     * 解密
//     *
//     * @param parameter  url绑定方法参数
//     * @param request    原始请求
//     * @param mediaType  媒体类型
//     * @param paramClass 参数类型
//     * @return
//     */
//    @Override
//    public NativeWebRequest decode(MethodParameter parameter, NativeWebRequest request, MediaType mediaType, Class<?> paramClass) {
//
//        IDecrypt decrypt = decryptManager.get();
//        /**
//         *  是否整体解密？,表单提交貌似没有整体解密的，除非是url上面
//         */
//        if (!valueModel) {
//            HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
//            String param = servletRequest.getQueryString();
//            if(StrUtil.isBlank(param)){
//                log.warn("解密表提交失败,提交数据为空");
//                return request;
//            }
//            try {
//                String decryptStr = decrypt.decryptStr(param);
//
//                /**
//                 *  不支持非json格式
//                 */
//                if(!StrKit.isJson(decryptStr)){
//                    throw new DecryptException("Unsupported non-json format");
//                }
//                /**
//                 *  提交的数据不应该是一个数组
//                 */
//                if(StrKit.isJsonArray(decryptStr)){
//                    throw new DecryptException("Submitted data should be key-value, not an array");
//                }
//
//                return new CopyNativeWebRequest(request,new ReplaceParamHttpServletRequest(servletRequest,decryptStr,true));
//            }catch (Exception e){
//                throw new DecryptException("Decrypted form submission count exception",e);
//            }
//
//        }
//
//
//        /**
//         *  无法确认使用的注解，所以只能用参数名去查参数
//         */
//        String parameterName = parameter.getParameterName();
//        String param = request.getParameter(parameterName);
//        if(StrUtil.isBlank(param)){
//            log.warn("解密表提交失败,参数:{}为空",parameterName);
//            return request;
//        }
//
//        String decryptStr = decrypt.decryptStr(param);
//
//
//        if(StrUtil.isNotBlank(decryptStr)){
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            if(parameterMap != null){
//                parameterMap.put(parameterName,new String[]{decryptStr});
//            }
//            return new CopyNativeWebRequest(request,new ReplaceParamHttpServletRequest(request.getNativeRequest(HttpServletRequest.class),parameterMap,false));
//        }
//
//        return request;
//    }







}
