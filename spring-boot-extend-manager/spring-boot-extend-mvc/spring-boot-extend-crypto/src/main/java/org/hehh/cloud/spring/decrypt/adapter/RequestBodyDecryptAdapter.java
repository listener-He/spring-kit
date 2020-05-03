package org.hehh.cloud.spring.decrypt.adapter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.common.utils.StrKit;
import org.hehh.cloud.spring.decrypt.DecryptManager;
import org.hehh.cloud.spring.decrypt.IDecrypt;
import org.hehh.cloud.spring.decrypt.annotation.Decrypt;
import org.hehh.cloud.spring.decrypt.param.DecryptParameter;
import org.hehh.cloud.spring.mvc.request.ReplaceInputStreamHttpServletRequest;
import org.hehh.cloud.spring.mvc.crypto.IDecryptAdapter;
import org.hehh.cloud.spring.mvc.util.ObjectMapperKit;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author: HeHui
 * @create: 2020-03-23 00:40
 * @description: body参数解析
 **/
@Slf4j
public class RequestBodyDecryptAdapter extends IDecryptAdapter {


    public RequestBodyDecryptAdapter(DecryptManager decryptManager) {
        super(decryptManager, false, true, Decrypt.class);
    }


    public RequestBodyDecryptAdapter(DecryptManager decryptManager, DecryptParameter decryptParameter) {
        super(decryptManager, decryptParameter.isValueModel(), decryptParameter.isScanAnnotation(), decryptParameter.getAnnotation());
    }


    /**
     * 是否支持解密
     *
     * @param mediaType 请求内容类型
     * @return
     */
    @Override
    public boolean supportsDecrypt(MediaType mediaType) {
        return mediaType != null && mediaType.includes(MediaType.APPLICATION_JSON);
    }


    /**
     * 此处新增代码
     * Create a new {@link HttpInputMessage} from the given {@link HttpServletRequest}.
     *
     * @param request the web request to create an input message from
     * @return the input message
     */
    protected HttpInputMessage createInputMessage(HttpServletRequest request) {
        return new ServletServerHttpRequest(request);
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

        HttpInputMessage inputMessage = createInputMessage(request);

        /**
         *  是否整体解密 ？
         */
        if (!super.isValueModel()) {

            byte[] data_byte = null;

            try {
                /**
                 *  解密
                 */
                data_byte = decrypt.decrypt(inputMessage.getBody());
            } catch (Exception e) {
                log.error("解密body失败:{}", e);
            }

            if (data_byte == null) {
                throw new HttpMessageNotReadableException("request body decrypt error,param body is null.", inputMessage);
            }
            return new ReplaceInputStreamHttpServletRequest(request, data_byte);
        }


        /**
         *  读取数据
         */
        String jsonStr = null;
        try {
            jsonStr = IoUtil.read(inputMessage.getBody(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StrUtil.isBlank(jsonStr)) {
            throw new HttpMessageNotReadableException("request body decrypt error,param body is null.", inputMessage);
        }


        /**
         *  验证是否为body格式
         */
        if (!StrKit.isJson(jsonStr)) {
            throw new HttpMessageNotReadableException("request body decrypt error,param body is not json format.", inputMessage);
        }


        if (StrKit.isJsonArray(jsonStr)) {
            log.error("暂时不支持集合解密");
            return request;
        }


        try {


            /**
             *  字符串转json
             */
            Map<String, Object> jsonMap = ObjectMapperKit.json2MapRecursion(jsonStr);


            for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
                Decrypt annotation = parameter.getParameterAnnotation(super.getAnnotation());
                if (annotation != null) {
                    String k = StringUtils.hasText(annotation.value()) ? annotation.value() : (StringUtils.hasText(parameter.getParameterName()) ? parameter.getParameterName() :  parameter.getParameter().getName());
                    Object v = jsonMap.get(k);
                    if (v == null) {
                        log.warn("字段:{}值为空,无法解密.", k);
                    } else {
                        if (v instanceof String) {
                            v = decrypt.decryptStr(v.toString());
                            jsonMap.put(k,v);
                        } else {
                            log.warn("字段:{},并未加密or不是值不是一个字符串.值:{}", k, v);
                        }

                    }

                }
            }

            return new ReplaceInputStreamHttpServletRequest(request, StrKit.hex2Byte(ObjectMapperKit.toJsonStr(jsonMap)));
        } catch (Exception e) {
            log.error("解密body,json TO map时异常:{}", e);
        }

        return request;
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
//        HttpInputMessage inputMessage = createInputMessage(request);
//        IDecrypt decrypt = decryptManager.get();
//
//        /**
//         *  是否整体解密 ？
//         */
//        if (!valueModel) {
//
//            byte[] data_byte = null;
//
//            try {
//                /**
//                 *  解密
//                 */
//                data_byte = decrypt.decrypt(inputMessage.getBody());
//            } catch (Exception e) {
//                log.error("解密body失败:{}", e);
//            }
//
//            if (data_byte == null) {
//                throw new HttpMessageNotReadableException("request body decrypt error,param body is null.", inputMessage);
//            }
//            return new CopyNativeWebRequest(request, new ReplaceInputStreamHttpServletRequest(request.getNativeRequest(HttpServletRequest.class), data_byte));
//
//        }
//
//
//        /**
//         *  读取数据
//         */
//        String jsonStr = null;
//        try {
//            jsonStr = IoUtil.read(inputMessage.getBody(), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (StrUtil.isBlank(jsonStr)) {
//            throw new HttpMessageNotReadableException("request body decrypt error,param body is null.", inputMessage);
//        }
//
//
//        /**
//         *  验证是否为body格式
//         */
//        if (!StrKit.isJson(jsonStr)) {
//            throw new HttpMessageNotReadableException("request body decrypt error,param body is not json format.", inputMessage);
//        }
//
//
//        if (StrKit.isJsonArray(jsonStr)) {
//            log.error("暂时不支持集合解密");
//            return request;
//        }
//
//
//        try {
//
//
//            /**
//             *  字符串转json
//             */
//            Map<String, Object> jsonMap = ObjectMapperKit.json2MapRecursion(jsonStr);
//
//            /**
//             *  循环字段
//             */
//            for (Field field : paramClass.getDeclaredFields()) {
//
//
//                /**
//                 *  需要解密的注解
//                 */
//                DecryptField decryptField = field.getAnnotation(DecryptField.class);
//                if (decryptField != null) {
//
//                    /**
//                     *  获取字段值
//                     */
//                    Object v = jsonMap.get(field.getName());
//                    if (v == null) {
//                        log.warn("字段:{}值为空,无法解密.", field.getName());
//                    } else {
//                        if (v instanceof String) {
//                            jsonMap.put(field.getName(), decrypt.decryptStr(v.toString()));
//                        } else {
//                            log.warn("字段:{},并未加密or不是值不是一个字符串.值:{}", field.getName(), v);
//                        }
//
//                    }
//
//
//                }
//            }
//
//            return new CopyNativeWebRequest(request, new ReplaceInputStreamHttpServletRequest(request.getNativeRequest(HttpServletRequest.class), StrKit.hex2Byte(ObjectMapperKit.toJsonStr(jsonMap))));
//            // return new DecryptHttpInputMessage(new ByteArrayInputStream(StrKit.hex2Byte(ObjectMapperKit.toJsonStr(jsonMap))), inputMessage.getHeaders());
//
//        } catch (Exception e) {
//            log.error("解密body,json TO map时异常:{}", e);
//        }
//
//        return request;
//    }
}
