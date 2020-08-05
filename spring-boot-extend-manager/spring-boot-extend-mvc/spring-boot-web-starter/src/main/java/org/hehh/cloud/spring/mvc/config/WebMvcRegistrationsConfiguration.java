package org.hehh.cloud.spring.mvc.config;

import org.hehh.cloud.spring.core.CoreConfiguration;
import org.hehh.cloud.spring.mvc.request.method.HandelMethodAdapterManager;
import org.hehh.cloud.spring.mvc.request.argument.HandlerMethodArgumentResolverEnhanceComposite;
import org.hehh.cloud.spring.mvc.request.method.IHandlerMethodAdapter;
import org.hehh.cloud.spring.mvc.request.argument.IHandlerMethodArgumentResolverAdapter;
import org.hehh.cloud.spring.mvc.util.JsonDateFormat;
import org.hehh.cloud.spring.mvc.util.ObjectMapperKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerEnhanceAdapter;

import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * @author: HeHui
 * @create: 2020-03-22 18:55
 * @description: spring mvc注册配置
 **/
@AutoConfigureAfter(CoreConfiguration.class)
@Import(CoreConfiguration.class)
public class WebMvcRegistrationsConfiguration  implements WebMvcRegistrations {



    private HandlerMethodArgumentResolverEnhanceComposite resolverEnhanceComposite;


    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    private HandelMethodAdapterManager methodAdapterManager;


//    @Autowired(required = false)
//    public  WebMvcRegistrationsConfiguration(List<IHandlerMethodArgumentResolverAdapter> resolverAdapters) {
//        if (!CollectionUtils.isEmpty(resolverAdapters)) {
//            resolverEnhanceComposite = new HandlerMethodArgumentResolverEnhanceComposite(resolverAdapters);
//        }
//    }


    @Autowired(required = false)
    public  WebMvcRegistrationsConfiguration(List<ResolverAdapterConfiguration> resolverAdapterConfigurations) {
        if (!CollectionUtils.isEmpty(resolverAdapterConfigurations)) {
             List<IHandlerMethodArgumentResolverAdapter> resolverAdapters = new LinkedList<>();
             List<IHandlerMethodAdapter> methodAdapters = new LinkedList<>();


            resolverAdapterConfigurations.forEach(v-> {
                v.addResolverAdapters(resolverAdapters);
                v.addMethodAdapters(methodAdapters);
            });

            resolverEnhanceComposite = new HandlerMethodArgumentResolverEnhanceComposite(resolverAdapters);
            if(!CollectionUtils.isEmpty(methodAdapters)){
                methodAdapterManager = new HandelMethodAdapterManager();
                methodAdapterManager.addResolvers(methodAdapters);
            }
        }
    }





    /**
     * Return the custom {@link RequestMappingHandlerAdapter} that should be used and
     * processed by the MVC configuration.
     *
     * @return the custom {@link RequestMappingHandlerAdapter} instance
     */
    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        if(requestMappingHandlerAdapter != null){
            return requestMappingHandlerAdapter;
        }
        this.requestMappingHandlerAdapter =  new RequestMappingHandlerEnhanceAdapter(resolverEnhanceComposite,methodAdapterManager);
        return requestMappingHandlerAdapter;
    }



}
