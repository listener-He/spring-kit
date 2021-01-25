package org.hehh.utils.file.pdf;

import org.hehh.cloud.common.bean.result.ErrorResult;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.common.bean.result.SuccessResult;
import org.hehh.utils.file.FileUtil;
import org.hehh.utils.file.watermar.WatermarkParam;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author: HeHui
 * @date: 2021-01-25 15:02
 * @description: 简单的生成pdf处理器
 */
public class SimpGeneratorPdfHandler implements GeneratorPdfHandler {


    /**
     * 创建
     *
     * @param content   内容
     * @param file      文件
     * @param watermark 水印
     *
     * @return {@link Result <String>}
     */
    @Override
    public Result<String> create(String content, File file, WatermarkParam... watermark) {
        /**
         *  html转pdf
         */
        if (StringUtils.hasText(content)) {
            PdfGenerator pdfGenerator = PdfGeneratorIText.getInstance();
            if (!FileUtil.getType(file).equalsIgnoreCase("pdf")) {
                return ErrorResult.error("文件类型非法");
            }
            try {
                if (pdfGenerator.create(content, file, watermark)) {
                    return SuccessResult.succeed(FileUtil.getCanonicalPath(file));
                }
            } catch (FileNotFoundException e) {
            }
        }
        return ErrorResult.error("生成pdf失败，无内容");
    }
}
