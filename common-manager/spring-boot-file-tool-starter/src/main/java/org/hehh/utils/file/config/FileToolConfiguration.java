package org.hehh.utils.file.config;

import org.hehh.utils.file.ftl.ConfigurationBuilderFactory;
import org.hehh.utils.file.ftl.ConfigurationCacheBuilderFactory;
import org.hehh.utils.file.ftl.DefaultGeneratorHtmlHandler;
import org.hehh.utils.file.ftl.GeneratorHtmlHandler;
import org.hehh.utils.file.pdf.DefaultFreemarkerGeneratorPdfHandler;
import org.hehh.utils.file.pdf.GeneratorPdfHandler;
import org.hehh.utils.file.pdf.SimpGeneratorPdfHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2021-01-27 14:01
 * @description: 文件工具配置
 */
@Configuration
public class FileToolConfiguration {

    /**
     * 参数
     *
     * @return {@link FileToolParameters}
     */
    @Bean
    @ConfigurationProperties("file.tool")
    public FileToolParameters parameters() {
        return new FileToolParameters();
    }

    /**
     * freemarker配置
     *
     * @author hehui
     * @date 2021/01/27
     */
    @Configuration
    @ConditionalOnClass(freemarker.template.Configuration.class)
    static class FreemarkerConfiguration {

        /**
         * 配置构建器工厂
         *
         * @return {@link ConfigurationBuilderFactory}
         */
        @Bean
        public ConfigurationBuilderFactory configurationBuilderFactory() {
            return new ConfigurationCacheBuilderFactory();
        }

        /**
         * 生成器html处理程序
         *
         * @param configurationBuilderFactory 配置构建器工厂
         * @param parameters                  参数
         *
         * @return {@link GeneratorHtmlHandler}
         *
         * @throws IOException ioexception
         */
        @Bean
        public GeneratorHtmlHandler generatorHtmlHandler(ConfigurationBuilderFactory configurationBuilderFactory, FileToolParameters parameters) throws IOException {
            return new DefaultGeneratorHtmlHandler(configurationBuilderFactory, parameters.getFreemarkerTemplate());
        }
    }


    /**
     * pdf工具配置
     *
     * @author hehui
     * @date 2021/01/27
     */
    @Configuration
    @ConditionalOnClass(name = "com.itextpdf.html2pdf.HtmlConverter")
    static class PdfToolConfiguration {

        /**
         * 简单生成器pdf处理程序
         *
         * @return {@link GeneratorPdfHandler}
         */
        @Bean
        @Primary
        public GeneratorPdfHandler simpGeneratorPdfHandler() {
            return new SimpGeneratorPdfHandler();
        }

        /**
         * 默认freemarker生成器pdf处理程序
         *
         * @param generatorHtmlHandler 生成器html处理程序
         *
         * @return {@link GeneratorPdfHandler}
         */
        @Bean
        @ConditionalOnBean(GeneratorHtmlHandler.class)
        public GeneratorPdfHandler defaultFreemarkerGeneratorPdfHandler(GeneratorHtmlHandler generatorHtmlHandler) {
            return new DefaultFreemarkerGeneratorPdfHandler(generatorHtmlHandler);
        }
    }


}
