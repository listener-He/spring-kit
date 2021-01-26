package org.hehh.utils.file.ftl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.*;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2021-01-21 11:29
 * @description: 默认ftl生成html处理器
 */
public class DefaultGeneratorHtmlHandler implements GeneratorHtmlHandler {

    private final Configuration defaultConfiguration;

    private final ConfigurationBuilderFactory configurationBuilderFactory;


    /**
     * ftl 默认的版本
     */
    private final Version defaultVersion;

    /**
     * 默认生成器html处理程序
     *
     * @param configurationBuilderFactory 配置构建器工厂
     */
    public DefaultGeneratorHtmlHandler(ConfigurationBuilderFactory configurationBuilderFactory) {
        this(configurationBuilderFactory,"template");
    }

    /**
     * 默认生成器html处理程序
     *
     * @param configurationBuilderFactory 配置构建器工厂
     * @param classLoadTemp               类加载模板目录
     */
    public DefaultGeneratorHtmlHandler(ConfigurationBuilderFactory configurationBuilderFactory,String classLoadTemp) {
        this(configurationBuilderFactory,classLoadTemp,Configuration.VERSION_2_3_0);
    }

    /**
     * 默认生成器html处理程序
     *
     * @param configurationBuilderFactory 配置构建器工厂
     * @param defaultVersion              默认的版本
     */
    public DefaultGeneratorHtmlHandler(ConfigurationBuilderFactory configurationBuilderFactory,Version defaultVersion) {
        this(configurationBuilderFactory,"template",defaultVersion);
    }

    /**
     * 默认生成器html处理程序
     *
     * @param configurationBuilderFactory 配置构建器工厂
     * @param classLoadTemp               类加载模板目录
     * @param defaultVersion              默认的版本
     */
    public DefaultGeneratorHtmlHandler(ConfigurationBuilderFactory configurationBuilderFactory,String classLoadTemp,Version defaultVersion) {
        this.configurationBuilderFactory = configurationBuilderFactory;
        this.defaultVersion = defaultVersion;
        this.defaultConfiguration = configurationBuilderFactory.builder(this.getClass(), classLoadTemp, defaultVersion);
    }

    /**
     * 默认生成器html处理程序
     *
     * @param configuration 配置
     *
     * @throws IOException ioexception
     */
    public DefaultGeneratorHtmlHandler(Configuration configuration, ConfigurationBuilderFactory configurationBuilderFactory) throws IOException {
        this(configuration,configurationBuilderFactory,Configuration.VERSION_2_3_0);
    }


    /**
     * 默认生成器html处理程序
     *
     * @param configuration               配置
     * @param configurationBuilderFactory 配置构建器工厂
     * @param defaultVersion              默认的版本
     *
     * @throws IOException ioexception
     */
    public DefaultGeneratorHtmlHandler(Configuration configuration, ConfigurationBuilderFactory configurationBuilderFactory,Version defaultVersion) throws IOException {
        defaultConfiguration = configuration;
        this.defaultVersion = defaultVersion;
        this.configurationBuilderFactory = configurationBuilderFactory;
    }


    /**
     * 生成
     *
     * @param source       源目录
     * @param params       参数
     * @param filename     模版名
     * @param outputStream 输出到流
     *
     * @throws IOException       输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    @Override
    public void generator(String source, Map<String, Object> params, String filename, OutputStream outputStream) throws IOException, TemplateException {
        generator(configurationBuilderFactory.builder(source, defaultVersion), filename, params, new OutputStreamWriter(outputStream));
    }

    /**
     * 生成
     *
     * @param filename     模版名
     * @param params       参数
     * @param outputStream 输出到流
     *
     * @throws IOException       输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    @Override
    public void generator(String filename, Map<String, Object> params, OutputStream outputStream) throws IOException, TemplateException {
        generator(defaultConfiguration, filename, params, new OutputStreamWriter(outputStream));
    }

    /**
     * 生成
     *
     * @param source   源
     * @param params   参数
     * @param filename 模板名
     * @param outFile  to文件
     *
     * @throws IOException       输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    @Override
    public void generator(String source, Map<String, Object> params, String filename, File outFile) throws IOException, TemplateException {
        generator(configurationBuilderFactory.builder(source, defaultVersion), filename, params, new FileWriter(outFile));
    }

    /**
     * 生成
     *
     * @param params   参数
     * @param filename 模版名
     * @param outFile  to文件
     *
     * @throws IOException       输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    @Override
    public void generator(Map<String, Object> params, String filename, File outFile) throws IOException, TemplateException {
        generator(defaultConfiguration, filename, params, new FileWriter(outFile));
    }

    /**
     * 生成
     *
     * @param source   源
     * @param params   参数
     * @param filename 模板名
     *
     * @return {@link String} 输出到字符串
     *
     * @throws IOException       输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    @Override
    public String generator(String source, Map<String, Object> params, String filename) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        generator(configurationBuilderFactory.builder(source, defaultVersion), filename, params, writer);
        return writer.toString();
    }

    /**
     * 生成
     *
     * @param params   参数
     * @param filename 模版名
     *
     * @return {@link String} 输出到字符串
     *
     * @throws IOException       输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    @Override
    public String generator(Map<String, Object> params, String filename) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        generator(defaultConfiguration, filename, params, writer);
        return writer.toString();
    }

    /**
     * 生成
     *
     * @param params   参数
     * @param filename 模板名
     * @param out      输出
     *
     * @throws IOException       输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    @Override
    public void generator(Map<String, Object> params, String filename, Writer out) throws IOException, TemplateException {
        generator(defaultConfiguration, filename, params, out);
    }

    /**
     * 生成
     *
     * @param source   源
     * @param params   参数
     * @param filename 模板名
     * @param out      输出
     *
     * @throws IOException       输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    @Override
    public void generator(String source, Map<String, Object> params, String filename, Writer out) throws IOException, TemplateException {
        generator(configurationBuilderFactory.builder(source, defaultVersion), filename, params, out);
    }

    /**
     * 生成
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
