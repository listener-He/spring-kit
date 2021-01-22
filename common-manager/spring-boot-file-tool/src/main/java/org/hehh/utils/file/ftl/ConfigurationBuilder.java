package org.hehh.utils.file.ftl;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: HeHui
 * @date: 2021-01-22 11:20
 * @description: 配置构建器 (伪的)
 */
public class ConfigurationBuilder extends Configuration {


    private final String charset = "utf-8";



    /**
     * 配置构建器
     *
     * @param incompatibleImprovements 不兼容的改进
     */
    private ConfigurationBuilder(Version incompatibleImprovements) {
        super(incompatibleImprovements);
    }

    /**
     * 配置构建器
     *
     * @param builder 构建器
     */
    public ConfigurationBuilder(Builder builder) {
        super(builder.incompatibleImprovements);
        if (builder.templateLoader != null) {
            super.setTemplateLoader(builder.templateLoader);
        }
        super.setDefaultEncoding(charset);
    }





    /**
     * 配置构建器
     *
     * @param defaultConfiguration 默认配置
     */
    public ConfigurationBuilder(Configuration defaultConfiguration) {
        super(defaultConfiguration.getIncompatibleImprovements());
        if (defaultConfiguration.getTemplateLoader() != null) {
            super.setTemplateLoader(defaultConfiguration.getTemplateLoader());
        }
        super.setDefaultEncoding(defaultConfiguration.getDefaultEncoding());
    }


    /**
     * 配置构建器
     *
     * @param directory                目录
     * @param incompatibleImprovements 不兼容的改进
     *
     * @throws IOException ioexception
     */
    public ConfigurationBuilder(String directory, Version incompatibleImprovements) throws IOException {
        super(incompatibleImprovements);
        this.setDirectoryForTemplateLoading(ResourceUtils.getFile(directory));
        this.setDefaultEncoding(charset);
    }


    /**
     * 配置构建器
     *
     * @param directory                目录
     * @param incompatibleImprovements 不兼容的改进
     *
     * @throws IOException ioexception
     */
    public ConfigurationBuilder(File directory, Version incompatibleImprovements) throws IOException {
        super(incompatibleImprovements);
        this.setDirectoryForTemplateLoading(directory);
        this.setDefaultEncoding(charset);
    }


    /**
     * 配置构建器
     *
     * @param templateLoader           模板加载程序
     * @param incompatibleImprovements 不兼容的改进
     */
    public ConfigurationBuilder(TemplateLoader templateLoader, Version incompatibleImprovements) {
        super(incompatibleImprovements);
        this.setTemplateLoader(templateLoader);
        this.setDefaultEncoding(charset);
    }


    /**
     * 配置构建器
     *
     * @param resourceLoaderClass      资源加载器类
     * @param packagePath              包的路径
     * @param incompatibleImprovements 不兼容的改进
     *
     * @throws IOException ioexception
     */
    public ConfigurationBuilder(Class resourceLoaderClass, String packagePath, Version incompatibleImprovements) throws IOException {
        super(incompatibleImprovements);
        this.setClassForTemplateLoading(resourceLoaderClass, packagePath);
        this.setDefaultEncoding(charset);
    }


    /**
     * 配置构建器
     *
     * @param classLoader              类装入器
     * @param packagePath              包的路径
     * @param incompatibleImprovements 不兼容的改进
     *
     * @throws IOException ioexception
     */
    public ConfigurationBuilder(ClassLoader classLoader, String packagePath, Version incompatibleImprovements) throws IOException {
        super(incompatibleImprovements);
        this.setClassLoaderForTemplateLoading(classLoader, packagePath);
        this.setDefaultEncoding(charset);
    }


    /**
     * 指令
     *
     * @param name      的名字
     * @param directive 指令
     *
     * @return {@link ConfigurationBuilder}
     */
    public ConfigurationBuilder directive(String name, TemplateDirectiveModel directive) {
        this.setSharedVariable(name, directive);
        return this;
    }


    /**
     * 函数
     *
     * @param name  的名字
     * @param model 模型
     *
     * @return {@link ConfigurationBuilder}
     */
    public ConfigurationBuilder model(String name, TemplateMethodModelEx model) {
        this.setSharedVariable(name, model);
        return this;
    }


    /**
     * 设置
     *
     * @param properties 属性
     *
     * @return {@link ConfigurationBuilder}
     *
     * @throws TemplateException 模板异常
     */
    public ConfigurationBuilder settings(Properties properties) throws TemplateException {
        this.setSettings(properties);
        return this;
    }


    /**
     * 设置
     *
     * @param name  名字
     * @param value 值
     *
     * @return {@link ConfigurationBuilder}
     *
     * @throws TemplateException 模板异常
     */
    public ConfigurationBuilder setting(String name, String value) throws TemplateException {
        this.setSetting(name, value);
        return this;
    }


    /**
     * 构建器
     *
     * @author hehui
     * @date 2021/01/22
     */
    public static class Builder {

        /**
         * 不兼容的改进版本
         */
        private Version incompatibleImprovements;


        /**
         * 模板加载程序 单独使用
         */
        private TemplateLoader templateLoader;


        public Builder() {
        }

        public Builder(Version incompatibleImprovements) {
            this.incompatibleImprovements = incompatibleImprovements;
        }

        public Builder(Configuration configuration) {
            this.incompatibleImprovements = configuration.getIncompatibleImprovements();
            this.templateLoader = configuration.getTemplateLoader();
        }


        /**
         * 不兼容的改进
         *
         * @param incompatibleImprovements 不兼容的改进
         *
         * @return {@link Builder}
         */
        public Builder incompatibleImprovements(Version incompatibleImprovements) {
            this.incompatibleImprovements = incompatibleImprovements;
            return this;
        }

        /**
         * 目录
         *
         * @param directory 目录
         *
         * @return {@link Builder}
         *
         * @throws IOException ioexception
         */
        public Builder directory(File directory) throws IOException {
            return templateLoader(new FileTemplateLoader(directory));
        }

        /**
         * 目录
         *
         * @param directory 目录
         *
         * @return {@link Builder}
         *
         * @throws IOException ioexception
         */
        public Builder directory(String directory) throws IOException {
            return directory(ResourceUtils.getFile(directory));
        }

        /**
         * 模板加载程序
         *
         * @param templateLoader 模板加载程序
         *
         * @return {@link Builder}
         */
        public Builder templateLoader(TemplateLoader templateLoader) {
            this.templateLoader = templateLoader;
            return this;
        }

        /**
         * 类装入器
         *
         * @param classLoader 类装入器
         * @param packagePath 包的路径
         *
         * @return {@link Builder}
         */
        public Builder classLoader(ClassLoader classLoader, String packagePath) {
            return templateLoader(new ClassTemplateLoader(classLoader, packagePath));
        }

        /**
         * 类装入器
         *
         * @param resourceLoaderClass 资源加载器类
         * @param packagePath         包的路径
         *
         * @return {@link Builder}
         */
        public Builder classLoader(Class resourceLoaderClass, String packagePath) {
            return templateLoader(new ClassTemplateLoader(resourceLoaderClass, packagePath));
        }

        /**
         * 构建
         *
         * @return {@link ConfigurationBuilder}
         *
         * @throws IOException ioexception
         */
        public ConfigurationBuilder build() {
            return new ConfigurationBuilder(this);
        }
    }
}
