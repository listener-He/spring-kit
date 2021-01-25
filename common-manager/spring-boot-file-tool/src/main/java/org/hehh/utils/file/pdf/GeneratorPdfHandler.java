package org.hehh.utils.file.pdf;

import org.hehh.cloud.common.bean.result.Result;
import org.hehh.utils.file.watermar.WatermarkParam;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author: HeHui
 * @date: 2021-01-21 10:48
 * @description: 生成pdf处理器
 */
public interface GeneratorPdfHandler {


    /**
     * 创建
     *
     * @param content   内容
     * @param filename  文件
     * @param watermark 水印
     *
     * @return {@link Result<String>}
     */
    default Result<String> create(String content, String filename, WatermarkParam... watermark) throws FileNotFoundException {
        return this.create(content, ResourceUtils.getFile(filename), watermark);
    }

    /**
     * 创建
     *
     * @param content   内容
     * @param watermark 水印
     * @param file      文件
     *
     * @return {@link Result<String>}
     */
    Result<String> create(String content, File file, WatermarkParam... watermark);
}
