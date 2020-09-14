package org.hehh.cloud.spring.mvc.config;


import org.hehh.cloud.spring.mvc.parameter.SignatureParameter;
import org.hehh.cloud.spring.mvc.request.argument.IHandlerMethodArgumentResolverAdapter;
import org.hehh.cloud.spring.mvc.request.method.IHandlerMethodAdapter;
import org.hehh.cloud.spring.mvc.sign.SignAdapter;
import org.hehh.cloud.spring.mvc.sign.SignVerifyBuild;
import org.hehh.cloud.spring.mvc.sign.SimpSignSecretKey;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author: HeHui
 * @create: 2020-09-14 01:02
 * @description: 签名配置
 **/
@Configuration
@AutoConfigureAfter(CryptoConfigurerComposite.class)
@ConditionalOnProperty(prefix = "spring.request.signature",name = "enable",havingValue = "true")
@EnableConfigurationProperties(SignatureParameter.class)
public class SignatureConfiguration implements ResolverAdapterConfiguration {



    private final SignatureParameter signatureParameter;

    public SignatureConfiguration(SignatureParameter signatureParameter) {
        this.signatureParameter = signatureParameter;
    }




    /**
     * 添加请求方法切面适配器
     *
     * @param methodAdapters
     */
    @Override
    public void addMethodAdapters(List<IHandlerMethodAdapter> methodAdapters) {
       methodAdapters.add(new SignAdapter(SignVerifyBuild.build(signatureParameter.getModel(),signatureParameter.getIgnore()), new SimpSignSecretKey(signatureParameter.getAppSecret()), signatureParameter.getSignName(),signatureParameter.getTimeName(),signatureParameter.getAppName(),signatureParameter.getOverdueTime()));
    }






    /**
     * 添加请求参数解析器适配器
     *
     * @param resolverAdapters
     */
    @Override
    public void addResolverAdapters(List<IHandlerMethodArgumentResolverAdapter> resolverAdapters) {

    }
}
