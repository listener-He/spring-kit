package org.hehh.template.ftl.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.hehh.template.ftl.RemoteTemplateLoader;
import org.hehh.template.ftl.directive.BaseTemplateDirectiveModel;
import org.hehh.template.ftl.model.BaseTemplateModel;
import org.hehh.utils.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Properties;

/**
 * @author: HeHui
 * @create: 2019-11-20 10:46
 * @description: ftl默认配置
 **/
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(FtlParameter.class)
@ConditionalOnBean({FreeMarkerConfigurer.class})
@Slf4j
public class FtlConfig {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private WebApplicationContext applicationContext;




    @Autowired
    private FtlParameter ftlParamConfig;




    @PostConstruct
    public void freeMarkerConfigurer() {
        freemarker.template.Configuration configuration = freeMarkerConfigurer.getConfiguration();

        if(null != ftlParamConfig.getGlobalParam() && !ftlParamConfig.getGlobalParam().isEmpty()){
            try {
                configuration.setSharedVariables(ftlParamConfig.getGlobalParam());
            } catch (TemplateModelException e) {
                log.warn("ftl初始化异常:{}", e);
            }
        }

        setTag(configuration);
        setTemplateLoader(configuration);
    }

    private void setTemplateLoader(freemarker.template.Configuration configuration) {
        /**
         *  远程模版加载
         */
        RemoteTemplateLoader remoteTemplateLoader = null;
        if (StrUtil.isNotBlank(ftlParamConfig.getRemotePath()) && StrKit.isUrl(ftlParamConfig.getRemotePath())) {
            remoteTemplateLoader = new RemoteTemplateLoader(ftlParamConfig.getRemotePath());
            log.debug("ftl初始化 >>>> 加载远程模版:{}", ftlParamConfig.getRemotePath());
        }


        /**
         *  本地模版
         */
        ClassTemplateLoader classTemplateLoader = null;
        if (StrUtil.isNotBlank(ftlParamConfig.getPackagePath())) {
            classTemplateLoader = new ClassTemplateLoader(getClass(), ftlParamConfig.getPackagePath());
        }


        MultiTemplateLoader templateLoader = null;
        if (null != remoteTemplateLoader && null != classTemplateLoader) {
            templateLoader = new MultiTemplateLoader(new TemplateLoader[]{classTemplateLoader, remoteTemplateLoader});
        } else if (remoteTemplateLoader != null) {
            templateLoader = new MultiTemplateLoader(new TemplateLoader[]{remoteTemplateLoader});
        } else if (null != classTemplateLoader) {
            templateLoader = new MultiTemplateLoader(new TemplateLoader[]{classTemplateLoader});
        }

        if (null != templateLoader) {
            configuration.setTemplateLoader(templateLoader);
        }
    }

    private void setTag(freemarker.template.Configuration configuration) {
        /**
         * 注册所有自定义标签
         */
        Map<String, BaseTemplateDirectiveModel> tagsMap = applicationContext.getBeansOfType(BaseTemplateDirectiveModel.class);
        if (MapUtil.isNotEmpty(tagsMap)) {
            tagsMap.forEach((k, v) -> {
                if (v != null) {
                    configuration.setSharedVariable(v.tagName(), v);
                    if(log.isDebugEnabled()){
                        log.debug("ftl初始化 >>>> 加载自定义指令:{}", v.tagName());
                    }
                }
            });
        }

        /**
         *  注册自定义函数
         */
        Map<String, BaseTemplateModel> templateModelMap = applicationContext.getBeansOfType(BaseTemplateModel.class);
        if(null != templateModelMap){
            templateModelMap.forEach((k,v)->{
                configuration.setSharedVariable(v.name(),v);
                if(log.isDebugEnabled()){
                    log.debug("ftl初始化 >>>> 加载自定义函数:{}", v.name());
                }
            });
        }


        Properties properties = new Properties();
        freeMarkerConfigurer.setFreemarkerSettings(properties);
    }

}
