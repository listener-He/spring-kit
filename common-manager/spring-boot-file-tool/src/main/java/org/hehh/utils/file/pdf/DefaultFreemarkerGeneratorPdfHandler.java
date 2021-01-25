package org.hehh.utils.file.pdf;

import freemarker.template.TemplateException;
import org.hehh.utils.file.ftl.GeneratorHtmlHandler;
import java.io.IOException;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2021-01-25 15:00
 * @description: 默认的ftl生成pdf处理器
 */
public class DefaultFreemarkerGeneratorPdfHandler extends SimpGeneratorPdfHandler implements FreemarkerGeneratorPdfHandler  {

    private final GeneratorHtmlHandler generatorHtmlHandler;

    public DefaultFreemarkerGeneratorPdfHandler(GeneratorHtmlHandler generatorHtmlHandler) {
        this.generatorHtmlHandler = generatorHtmlHandler;
    }

    /**
     * 生成html
     *
     * @param source   源
     * @param template 模板
     * @param params   参数
     *
     * @return {@link String}
     */
    @Override
    public String generator(String source, String template, Map<String, Object> params) throws IOException, TemplateException {
        return generatorHtmlHandler.generator(source, params, template);
    }

    /**
     * 生成html
     *
     * @param template 模板
     * @param params   参数
     *
     * @return {@link String}
     */
    @Override
    public String generator(String template, Map<String, Object> params) throws IOException, TemplateException {
        return generatorHtmlHandler.generator(params, template);
    }


}
