package org.hehh.cloud.spring.decrypt.adapter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.hehh.cloud.common.utils.StrKit;
import org.hehh.cloud.spring.decrypt.IDecrypt;
import org.hehh.cloud.spring.decrypt.annotation.DecryptField;
import org.hehh.cloud.spring.decrypt.param.DecryptParameter;
import org.hehh.cloud.spring.mvc.copy.ReplaceInputStreamHttpServletRequest;
import org.hehh.cloud.spring.mvc.core.CacheRequestHttpInputMessage;
import org.hehh.cloud.spring.mvc.core.CopyNativeWebRequest;
import org.hehh.cloud.spring.mvc.crypto.IDecryptAdapter;
import org.hehh.cloud.spring.mvc.util.ObjectMapperKit;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author: HeHui
 * @create: 2020-03-23 00:40
 * @description: body参数解析
 **/
@Slf4j
public class RequestBodyDecryptAdapter implements IDecryptAdapter {




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


    public RequestBodyDecryptAdapter(IDecrypt decrypt) {
        this.decrypt = decrypt;
        valueModel = false;
        this.scanAnnotation = true;
    }


    public RequestBodyDecryptAdapter(IDecrypt decrypt, DecryptParameter decryptParameter) {
        this.decrypt = decrypt;
        this.scanAnnotation = decryptParameter.isScanAnnotation();
        this.valueModel = decryptParameter.isValueModel();
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
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsDecrypt(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    /**
     *   此处新增代码
     * Create a new {@link HttpInputMessage} from the given {@link NativeWebRequest}.
     * @param webRequest the web request to create an input message from
     * @return the input message
     */
    protected HttpInputMessage createInputMessage(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        return new CacheRequestHttpInputMessage(servletRequest);
    }


    /**
     * 解密
     *
     * @param parameter    url绑定方法参数
     * @param request      原始请求
     * @param mediaType    媒体类型
     * @param paramClass   参数类型
     * @return
     */
    @Override
    public NativeWebRequest decode(MethodParameter parameter, NativeWebRequest request, MediaType mediaType, Class<?> paramClass) {
        HttpInputMessage inputMessage = createInputMessage(request);

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

            return new CopyNativeWebRequest(request, new ReplaceInputStreamHttpServletRequest(request.getNativeRequest(HttpServletRequest.class),data_byte));

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

            /**
             *  循环字段
             */
            for (Field field : paramClass.getDeclaredFields()) {


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

            return new CopyNativeWebRequest(request, new ReplaceInputStreamHttpServletRequest(request.getNativeRequest(HttpServletRequest.class),StrKit.hex2Byte(ObjectMapperKit.toJsonStr(jsonMap))));
           // return new DecryptHttpInputMessage(new ByteArrayInputStream(StrKit.hex2Byte(ObjectMapperKit.toJsonStr(jsonMap))), inputMessage.getHeaders());

        } catch (Exception e) {
            log.error("解密body,json TO map时异常:{}", e);
        }

        return request;
    }
}
