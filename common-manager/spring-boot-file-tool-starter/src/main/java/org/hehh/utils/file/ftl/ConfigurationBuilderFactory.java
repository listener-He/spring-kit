package org.hehh.utils.file.ftl;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Version;

import java.io.File;
import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2021-01-22 14:45
 * @description: 配置构建工厂
 */
public interface ConfigurationBuilderFactory {

    /**
     * 构建器
     *
     * @param directory                目录
     * @param incompatibleImprovements 不兼容的改进
     *
     * @return {@link Configuration}
     *
     * @throws IOException ioexception
     */
    Configuration builder(String directory, Version incompatibleImprovements) throws IOException;

    /**
     * 构建器
     *
     * @param directory                目录
     * @param incompatibleImprovements 不兼容的改进
     *
     * @return {@link Configuration}
     *
     * @throws IOException ioexception
     */
    Configuration builder(File directory, Version incompatibleImprovements) throws IOException;

    /**
     * 构建器
     *
     * @param templateLoader           模板加载程序
     * @param incompatibleImprovements 不兼容的改进
     *
     * @return {@link Configuration}
     */
    @Deprecated
    Configuration builder(TemplateLoader templateLoader, Version incompatibleImprovements);

    /**
     * 构建器
     *
     * @param resourceLoaderClass      资源加载器类
     * @param packagePath              包的路径
     * @param incompatibleImprovements 不兼容的改进
     *
     * @return {@link Configuration}
     *
     * @throws IOException ioexception
     */
    Configuration builder(Class resourceLoaderClass, String packagePath, Version incompatibleImprovements);

    /**
     * 构建器
     *
     * @param classLoader              类装入器
     * @param packagePath              包的路径
     * @param incompatibleImprovements 不兼容的改进
     *
     * @return {@link Configuration}
     *
     * @throws IOException ioexception
     */
    Configuration builder(ClassLoader classLoader, String packagePath, Version incompatibleImprovements);
}
