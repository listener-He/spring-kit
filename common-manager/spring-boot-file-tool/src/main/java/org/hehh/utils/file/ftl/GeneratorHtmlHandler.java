package org.hehh.utils.file.ftl;


import freemarker.template.TemplateException;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Map;

/**
 * @author: HeHui
 * @date: 2021-01-21 10:56
 * @description: ftl生成pdf处理器
 */
public interface GeneratorHtmlHandler {


    /**
     * 生成
     *
     * @param source       源目录
     * @param params       参数
     * @param filename     模版名
     * @param outputStream 输出到流
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    void generator(String source, Map<String, Object> params, String filename, OutputStream outputStream) throws IOException, TemplateException;

    /**
     * 生成
     *
     * @param filename     模版名
     * @param params       参数
     * @param outputStream 输出到流
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    void generator(String filename, Map<String, Object> params, OutputStream outputStream) throws IOException, TemplateException;

    /**
     * 生成
     *
     * @param source   源
     * @param params   参数
     * @param filename 模板名
     * @param outFile  to文件
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    default void generator(String source, Map<String, Object> params, String filename, String outFile) throws IOException, TemplateException {
        generator(source, params, filename, ResourceUtils.getFile(outFile));
    }

    /**
     * 生成
     *
     * @param source   源
     * @param params   参数
     * @param filename 模板名
     * @param outFile  to文件
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    void generator(String source, Map<String, Object> params, String filename, File outFile) throws IOException, TemplateException;

    /**
     * 生成
     *
     * @param params   参数
     * @param filename 模版名
     * @param outFile  to文件
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    default void generator(Map<String, Object> params, String filename, String outFile) throws IOException, TemplateException {
        generator(params, filename, ResourceUtils.getFile(outFile));
    }

    /**
     * 生成
     *
     * @param params   参数
     * @param filename 模版名
     * @param outFile  to文件
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    void generator(Map<String, Object> params, String filename, File outFile) throws IOException, TemplateException;


    /**
     * 生成
     *
     * @param source   源
     * @param params   参数
     * @param filename 模板名
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     * @return {@link String} 输出到字符串
     */
    String generator(String source, Map<String, Object> params, String filename) throws IOException, TemplateException;

    /**
     * 生成
     *
     * @param params   参数
     * @param filename 模版名
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     * @return {@link String} 输出到字符串
     */
    String generator(Map<String, Object> params, String filename) throws IOException, TemplateException;

    /**
     * 生成
     *
     * @param params   参数
     * @param filename 模板名
     * @param out      输出
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    void generator(Map<String, Object> params, String filename, Writer out) throws IOException, TemplateException;

    /**
     * 生成
     *
     * @param source   源
     * @param params   参数
     * @param filename 模板名
     * @param out      输出
     * @throws IOException 输入输出文件IO异常
     * @throws TemplateException 生成模板异常
     */
    void generator(String source, Map<String, Object> params, String filename, Writer out) throws IOException, TemplateException;


}
