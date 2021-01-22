package org.hehh.utils.file.pdf;

import java.io.File;
import java.io.OutputStream;

/**
 * @author: HeHui
 * @date: 2020-09-07 13:53
 * @description: pdf文件
 */
public abstract class PDFDocument {


    /**
     * 创建
     *
     * @param pdfPath pdf文件路径
     */
    void writer(String pdfPath) {
        this.writer(new File(pdfPath));
    }


    /**
     * 创建
     *
     * @param pdfFile pdf文件
     */
    abstract void writer(File pdfFile);


    /**
     * 创建
     *
     * @return {@link OutputStream}
     */
    abstract void writer(OutputStream outputStream);
}
