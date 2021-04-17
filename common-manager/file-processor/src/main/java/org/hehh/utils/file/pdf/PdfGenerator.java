package org.hehh.utils.file.pdf;


import org.hehh.utils.file.watermar.WatermarkParam;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author: HeHui
 * @date: 2020-09-04 17:32
 * @description: pdf文档
 */
public interface PdfGenerator {


    /**
     * 字体路径
     * 设置中文字体文件的路径，可以在classpath目录下
     *
     * @return {@link String}
     */
    default String fontPath() {
        return "/fonts/Alibaba-PuHuiTi-Regular.otf";
    }

    /**
     * 创建
     *
     * @param html      超文本标记语言
     * @param pdf       pdf
     * @param watermark 水印
     *
     * @return boolean
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    default boolean create(String html, String pdf, WatermarkParam... watermark) throws FileNotFoundException {
        return create(html, new File(pdf), watermark);
    }


    /**
     * 创建
     *
     * @param html      超文本标记语言
     * @param pdf       pdf
     * @param watermark 水印
     *
     * @return boolean
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    boolean create(String html, File pdf, WatermarkParam... watermark) throws FileNotFoundException;
}
