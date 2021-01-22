package org.hehh.utils.file.ftl;


import java.io.File;
import java.io.OutputStream;
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
     * @param source   源目录
     * @param params   参数
     * @param filename 模版名
     *
     * @return {@link OutputStream}
     */
    OutputStream generator(String source, Map<String, Object> params, String filename);

    /**
     * 生成
     *
     * @param filename   模版名
     * @param params 参数
     *
     * @return {@link OutputStream }
     */
    OutputStream generator(String filename, Map<String, Object> params);


    /**
     *  生成
     *
     * @param source       源
     * @param params       参数
     * @param filename     模板名
     * @param outFile to文件
     *
     * @return {@link File}
     */
    File generator(String source, Map<String, Object> params, String filename, String outFile);

    /**
     *  生成
     *
     * @param params       参数个数
     * @param filename     文件名
     * @param outFile to文件
     *
     * @return {@link File}
     */
    File generator(Map<String, Object> params, String filename, String outFile);



}
