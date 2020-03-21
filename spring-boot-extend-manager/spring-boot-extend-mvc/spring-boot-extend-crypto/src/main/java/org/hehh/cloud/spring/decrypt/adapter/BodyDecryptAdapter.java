package org.hehh.cloud.spring.decrypt.adapter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.common.utils.StrKit;
import org.hehh.cloud.spring.decrypt.IDecrypt;
import org.hehh.cloud.spring.decrypt.annotation.Decrypt;
import org.hehh.cloud.spring.decrypt.annotation.DecryptField;
import org.hehh.cloud.spring.decrypt.param.DecryptParameter;
import org.hehh.cloud.spring.mvc.core.ObjectMapperKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author: HeHui
 * @create: 2020-03-21 15:25
 * @description: body请求解密
 **/
@Slf4j
@RestControllerAdvice
public class BodyDecryptAdapter extends RequestBodyAdviceAdapter {

    /**
     * 解密对象
     */
    private final IDecrypt decrypt;


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


    public BodyDecryptAdapter(IDecrypt decrypt) {
        this.decrypt = decrypt;
        valueModel = false;
        this.scanAnnotation = true;
    }


    @Autowired
    public BodyDecryptAdapter(IDecrypt decrypt, DecryptParameter decryptParameter) {
        this.decrypt = decrypt;
        this.scanAnnotation = decryptParameter.isScanAnnotation();
        this.valueModel = decryptParameter.isValueModel();
    }


    /**
     * 是否支持加密消息体
     *
     * @param methodParameter methodParameter
     * @return true/false
     */
    private boolean supportSecretRequest(MethodParameter methodParameter) {
        if (!scanAnnotation) {
            return true;
        }


        /**
         *  判断class是否存在注解 or 方法上面有没有注解 or 参数上面有没有注释
         */
        if (methodParameter.hasMethodAnnotation(Decrypt.class)
                || methodParameter.getContainingClass().getAnnotation(Decrypt.class) != null
                || methodParameter.hasParameterAnnotation(Decrypt.class)) {
            return true;
        }

        return false;

    }


    /**
     * 这里是一个前置拦截匹配操作,其实就是告诉你满足为true的才会执行下面的beforeBodyRead方法,这里可以定义自己业务相关的拦截匹配
     * 是否支持加密消息体
     *
     * @param methodParameter 方法参数
     * @param type            类型
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return supportSecretRequest(methodParameter);
    }


    /**
     * The default implementation returns the InputMessage that was passed in.
     * 在body读取之前
     *
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {


        /**
         *  是否整体解密 ？
         */
        if (!valueModel) {

            byte[] data_byte = null;

            try {
                /**
                 *  解密
                 */
                data_byte  = this.decrypt.decrypt(inputMessage.getBody());
            }catch (Exception e){
                log.error("解密body失败:{}", e);
            }

            if (data_byte == null) {
                throw new HttpMessageNotReadableException("request body decrypt error,param body is null.", inputMessage);
            }
            return new DecryptHttpInputMessage(new ByteArrayInputStream(data_byte), inputMessage.getHeaders());

        }


        /**
         *  读取数据
         */
        String jsonStr = IoUtil.read(inputMessage.getBody(), StandardCharsets.UTF_8);
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
            return inputMessage;
        }

        Class clazz = null;
        try {
            clazz = Class.forName(targetType.getTypeName());
        } catch (ClassNotFoundException e) {
            log.error("解密时获取参数类型异常:{}", e);
        }

        if (clazz == null) {
            return inputMessage;
        }


        try {


            /**
             *  字符串转json
             */
            Map<String, Object> jsonMap = ObjectMapperKit.json2MapRecursion(jsonStr);

            /**
             *  循环字段
             */
            for (Field field : clazz.getDeclaredFields()) {


                /**
                 *  需要解密的注解
                 */
                DecryptField decryptField = field.getAnnotation(DecryptField.class);
                if (decryptField != null) {

                    /**
                     *  获取字段值
                     */
                    Object v = jsonMap.get(field.getName());
                    if (v == null) {
                        log.warn("字段:{}值为空,无法解密.", field.getName());
                    } else {
                        if (v instanceof String) {
                            jsonMap.put(field.getName(), decrypt.decryptStr(v.toString()));
                        } else {
                            log.warn("字段:{},并未加密or不是值不是一个字符串.值:{}", field.getName(), v);
                        }

                    }


                }
            }

            return new DecryptHttpInputMessage(new ByteArrayInputStream(StrKit.hex2Byte(ObjectMapperKit.toJsonStr(jsonMap))), inputMessage.getHeaders());

        } catch (Exception e) {
            log.error("解密body,json TO map时异常:{}", e);
        }

        return inputMessage;


    }


}
