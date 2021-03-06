package org.hehh.cloud.spring.mvc.crypto;

import org.hehh.cloud.spring.mvc.config.ResolverAdapterConfiguration;
import org.hehh.cloud.spring.mvc.request.method.IHandlerMethodAdapter;
import org.hehh.cloud.spring.mvc.request.argument.IHandlerMethodArgumentResolverAdapter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: HeHui
 * @create: 2020-03-23 01:02
 * @description:
 **/
public interface CryptoConfiguration extends ResolverAdapterConfiguration {


    /**
     *  获取解密适配器
     * @return
     */
    List<IDecryptAdapter> getDecryptAdapters();





    /**
     * 添加请求方法切面适配器
     *
     * @param methodAdapters
     */
    @Override
    default void addMethodAdapters(List<IHandlerMethodAdapter> methodAdapters){
        List<IDecryptAdapter> decryptAdapters = getDecryptAdapters();
        if(!CollectionUtils.isEmpty(decryptAdapters)){
            methodAdapters.addAll(decryptAdapters);
        }
    }






    /**
     * 添加请求参数解析器适配器
     *
     * @param resolverAdapters
     */
    @Override
    default void addResolverAdapters(List<IHandlerMethodArgumentResolverAdapter> resolverAdapters){ }
}
