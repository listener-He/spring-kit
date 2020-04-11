package org.hehh.cloud.spring.mvc.crypto;


import lombok.Getter;
import org.hehh.cloud.spring.decrypt.DecryptManager;
import org.hehh.cloud.spring.decrypt.IDecrypt;
import org.hehh.cloud.spring.decrypt.annotation.ChooseDecrypt;
import org.hehh.cloud.spring.decrypt.annotation.Decrypt;
import org.hehh.cloud.spring.exception.DecryptException;
import org.hehh.cloud.spring.mvc.core.HandlerMethodArgumentResolverEnhanceComposite;
import org.hehh.cloud.spring.mvc.core.IHandlerMethodAdapter;
import org.hehh.cloud.spring.mvc.core.IHandlerMethodArgumentResolverAdapter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: HeHui
 * @create: 2020-03-22 17:14
 * @description: 解密适配器
 **/
public abstract class IDecryptAdapter implements IHandlerMethodAdapter {


    /**
     * 解密对象
     */
    private final DecryptManager decryptManager;

    /**
     * 是否值解密模式
     * true：只解密key对应的值，客户端请求还需正常的json格式，只是对value进行加密
     * false: 客户端请求参数是一个被加密后对json格式，服务端对字符串进行解密
     */
    @Getter
    private final boolean valueModel;


    /**
     * 是否需要扫描注解才解密
     */
    @Getter
    private final boolean scanAnnotation;

    /**
     *  被扫描的注解
     */
    @Getter
    private final Class<? extends Decrypt> annotation;



    public IDecryptAdapter(DecryptManager decryptManager,boolean valueModel,boolean scanAnnotation,Class<? extends Decrypt> annotation){
        this.decryptManager = decryptManager;
        this.valueModel = valueModel;
        this.scanAnnotation = scanAnnotation;
        this.annotation = annotation;
    }








    /**
     *  是否支持解密
     * @param mediaType 请求内容类型
     * @return
     */
    protected abstract boolean supportsDecrypt(MediaType mediaType);


    /**
     * 在参数绑定之前处理
     *
     * @param request       原始请求
     * @param handlerMethod 适配方法
     * @return
     */
    @Override
    public HttpServletRequest beforeResolver(HttpServletRequest request, HandlerMethod handlerMethod) {

        ChooseDecrypt chooseDecrypt = handlerMethod.getMethodAnnotation(ChooseDecrypt.class);

        try {
            return decode(((null == chooseDecrypt || !StringUtils.hasText(chooseDecrypt.value())) ? decryptManager.get() : decryptManager.get(chooseDecrypt.value())), request, handlerMethod);
        }catch (Exception e){
            if( e instanceof DecryptException){
                throw e;
            }
            throw new DecryptException("Decrypted form submission count exception",e);
        }
    }


    /**
     *  解密
     * @param decrypt 解密器
     * @param request 原始请求
     * @param handlerMethod 绑定方法
     * @return
     */
    protected abstract HttpServletRequest decode(IDecrypt decrypt, HttpServletRequest request, HandlerMethod handlerMethod);





    /**
     * 判断是否支持适配
     *
     * @param handlerMethod the method  to check
     * @param mediaType     request  mediaType
     * @return {@code true} if this resolver supports the supplied method;
     * {@code false} otherwise
     */
    @Override
    public boolean supportsMethod(HandlerMethod handlerMethod, MediaType mediaType){
        boolean supportsDecrypt = supportsDecrypt(mediaType);
        if (!isScanAnnotation() && supportsDecrypt) {
            return true;
        }


        /**
         *  判断class是否存在注解 or 方法上面有没有注解 or 参数上面有没有注释
         */
        if (supportsDecrypt) {
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            if(methodParameters != null){
                for (MethodParameter parameter : methodParameters) {
                    return parameter.hasParameterAnnotation(annotation);
                }
            }

        }

        return false;
    }


}
