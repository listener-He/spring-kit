package org.hehh.utils.file.pdf;

import freemarker.template.TemplateException;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.utils.file.watermar.WatermarkParam;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2021-01-21 10:54
 * @description: ftl生成pdf处理器
 */
public interface FreemarkerGeneratorPdfHandler extends GeneratorPdfHandler {


    /**
     * 创建
     *
     * @param template  模版名
     * @param filename  文件名
     * @param watermark 水印
     * @param params    参数
     *
     * @return {@link Result<String>}
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    default Result<String> create(String template, String filename, Map<String, Object> params, WatermarkParam... watermark) throws IOException, TemplateException {
        return create(template, ResourceUtils.getFile(filename), params, watermark);
    }


    /**
     * 创建
     *
     * @param source    源
     * @param template  模板
     * @param filename  文件名
     * @param watermark 水印
     * @param params    参数
     *
     * @return {@link Result<String>}
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    default Result<String> create(String source, String template, String filename, Map<String, Object> params, WatermarkParam... watermark) throws IOException, TemplateException {
        return create(source, template, ResourceUtils.getFile(filename), params, watermark);
    }


    /**
     * 创建
     *
     * @param watermark 水印
     * @param file      文件
     * @param source    模板源
     * @param template  模板
     * @param params    参数
     *
     * @return {@link Result<String>}
     */
    default Result<String> create(String source, String template, File file, Map<String, Object> params, WatermarkParam... watermark) throws IOException, TemplateException {
        return create(generator(source, template, params), file, watermark);
    }

    /**
     * 创建
     *
     * @param template  模板
     * @param file      文件
     * @param watermark 水印
     * @param params    参数
     *
     * @return {@link Result<String>}
     */
    default Result<String> create(String template, File file, Map<String, Object> params, WatermarkParam... watermark) throws IOException, TemplateException {
        return create(generator(template, params), file, watermark);
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
    String generator(String source, String template, Map<String, Object> params) throws IOException, TemplateException;

    /**
     * 生成html
     *
     * @param template 模板
     * @param params   参数
     *
     * @return {@link String}
     */
    String generator(String template, Map<String, Object> params) throws IOException, TemplateException;
}
