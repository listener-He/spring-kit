package org.hehh.utils.file.ftl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.hehh.cloud.common.bean.result.Result;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2021-01-21 11:29
 * @description: 默认ftl生成html处理器
 */
public class DefaultGeneratorHtmlHandler implements GeneratorHtmlHandler {

    private final Configuration defaultConfiguration;
    private final ConfigurationBuilderFactory configurationBuilderFactory;
    private final Version defaultVersion = Configuration.VERSION_2_3_0;

    public DefaultGeneratorHtmlHandler(ConfigurationBuilderFactory configurationBuilderFactory) {
        this.configurationBuilderFactory = configurationBuilderFactory;
        this.defaultConfiguration = configurationBuilderFactory.builder(this.getClass(), "template", defaultVersion);
    }

    /**
     * 默认生成器html处理程序
     *
     * @param builder 构建器
     *
     * @throws IOException ioexception
     */
    public DefaultGeneratorHtmlHandler(ConfigurationBuilder builder, ConfigurationBuilderFactory configurationBuilderFactory) throws IOException {
        defaultConfiguration = builder;
        this.configurationBuilderFactory = configurationBuilderFactory;
    }


    /**
     * 默认生成器html处理程序
     *
     * @param configuration 配置
     *
     * @throws IOException ioexception
     */
    public DefaultGeneratorHtmlHandler(Configuration configuration, ConfigurationBuilderFactory configurationBuilderFactory) throws IOException {
        defaultConfiguration = configuration;
        this.configurationBuilderFactory = configurationBuilderFactory;
    }







    /**
     *  生成
     *
     * @param configuration 配置
     * @param filename      文件名
     * @param params        参数个数
     * @param out           出
     *
     * @throws IOException       ioexception
     * @throws TemplateException 模板异常
     */
    private void generator(Configuration configuration, String filename, Map<String, Object> params, Writer out) throws IOException, TemplateException {
        try {
            Template template = configuration.getTemplate(filename);
            template.process(params, out);
        } finally {
            out.close();
        }
    }
}
