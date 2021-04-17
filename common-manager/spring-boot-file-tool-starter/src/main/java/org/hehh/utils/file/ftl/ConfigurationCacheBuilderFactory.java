package org.hehh.utils.file.ftl;


import freemarker.cache.*;
import freemarker.template.Configuration;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author: HeHui
 * @date: 2021-01-22 13:59
 * @description: 配置构建缓存
 */
@Slf4j
public class ConfigurationCacheBuilderFactory implements ConfigurationBuilderFactory {


    private final Map<TemplateLoader, Configuration> cache = new ConcurrentHashMap<>();

    /**
     * 配置缓存构建器工厂
     */
    public ConfigurationCacheBuilderFactory() {
    }

    /**
     * 配置缓存构建器工厂
     *
     * @param configurations 配置
     */
    public ConfigurationCacheBuilderFactory(Configuration... configurations) {
        for (Configuration configuration : configurations) {
            cache.put(configuration.getTemplateLoader(), configuration);
        }
    }

    /**
     * 获取缓存
     *
     * @param templateLoader 模板加载程序
     * @param supplier       供应商
     *
     * @return {@link Configuration}
     */
    private Configuration getCache(TemplateLoader templateLoader, Supplier<Configuration> supplier) {
        Optional<Configuration> optional = cache.entrySet().stream().filter(entry -> {
            try {
                if (templateLoader.equals(entry.getKey())) {
                    return true;
                }
                if (entry.getKey().getClass().equals(templateLoader.getClass())) {

                    if (entry.getKey() instanceof FileTemplateLoader &&
                        (((FileTemplateLoader) entry.getKey()).baseDir.getCanonicalPath().equals(((FileTemplateLoader) templateLoader).baseDir.getCanonicalPath()))) {
                        return true;
                    } else if (entry.getKey() instanceof ClassTemplateLoader) {
                        ClassTemplateLoader k = (ClassTemplateLoader) entry.getKey();
                        ClassTemplateLoader t = (ClassTemplateLoader) templateLoader;
                        if (k.getResourceLoaderClass().equals(t.getResourceLoaderClass()) && k.getBasePackagePath().equals(t.getBasePackagePath())) {
                            return true;
                        }
                    } else if (entry.getKey() instanceof StringTemplateLoader) {
                        /**此处没什么可判断的*/
                        return true;
                    } else if (entry.getKey() instanceof MultiTemplateLoader) {
                        /**类型太多不好判断，直接返回true*/
                        if (log.isDebugEnabled()) {
                            log.debug("不想判断MultiTemplateLoader");
                        }
                        return true;
                    } else if (entry.getKey() instanceof SpringTemplateLoader) {
                        if (log.isDebugEnabled()) {
                            log.debug("不想判断SpringTemplateLoader");
                        }
                        return true;
                    }

                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }).map(Map.Entry::getValue).findFirst();

        return optional.orElseGet(() -> {
            Configuration configuration = supplier.get();
            cache.put(configuration.getTemplateLoader(), configuration);
            return configuration;
        });
    }

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
    @Override
    public Configuration builder(String directory, Version incompatibleImprovements) throws IOException {
        FileTemplateLoader templateLoader = new FileTemplateLoader(ResourceUtils.getFile(directory));
        return getCache(templateLoader, () -> {
            return new ConfigurationBuilder.Builder(incompatibleImprovements).templateLoader(templateLoader).build();
        });

    }

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
    @Override
    public Configuration builder(File directory, Version incompatibleImprovements) throws IOException {
        FileTemplateLoader templateLoader = new FileTemplateLoader(directory);
        return getCache(templateLoader, () -> {
            return new ConfigurationBuilder.Builder(incompatibleImprovements).templateLoader(templateLoader).build();
        });
    }

    /**
     * 构建器
     *
     * @param templateLoader           模板加载程序
     * @param incompatibleImprovements 不兼容的改进
     *
     * @return {@link Configuration}
     */
    @Override
    public Configuration builder(TemplateLoader templateLoader, Version incompatibleImprovements) {
        return getCache(templateLoader, () -> {
            return new ConfigurationBuilder.Builder(incompatibleImprovements).templateLoader(templateLoader).build();
        });
    }

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
    @Override
    public Configuration builder(Class resourceLoaderClass, String packagePath, Version incompatibleImprovements) {
        ClassTemplateLoader templateLoader = new ClassTemplateLoader(resourceLoaderClass, packagePath);
        return getCache(templateLoader, () -> {
            return new ConfigurationBuilder.Builder(incompatibleImprovements).templateLoader(templateLoader).build();
        });
    }

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
    @Override
    public Configuration builder(ClassLoader classLoader, String packagePath, Version incompatibleImprovements) {
        ClassTemplateLoader templateLoader = new ClassTemplateLoader(classLoader, packagePath);
        return getCache(templateLoader, () -> {
            return new ConfigurationBuilder.Builder(incompatibleImprovements).templateLoader(templateLoader).build();
        });
    }
}
