package org.hehh.utils.file.pdf;

import org.hehh.cloud.common.bean.result.Result;
import org.hehh.utils.file.watermar.WatermarkParam;

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
     * @param filename  文件名
     * @param watermark 水印
     *
     * @return {@link Result<String>}
     */
    default Result<String> create(String content, String filename, WatermarkParam... watermark) {
        return this.create(content, filename, null, watermark);
    }

    /**
     * 创建
     *
     * @param content   内容
     * @param filename  文件名
     * @param directory 目录
     * @param watermark 水印
     *
     * @return {@link Result<String>}
     */
    Result<String> create(String content, String filename, String directory, WatermarkParam... watermark);
}
