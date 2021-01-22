package org.hehh.utils.file.pdf;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.OutlineHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import lombok.extern.slf4j.Slf4j;
import org.hehh.utils.file.pdf.event.WaterMarker;
import org.hehh.utils.file.watermar.WatermarkParam;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author: HeHui
 * @date: 2020-09-04 18:21
 * @description: pdf 文档 itext实现  对html格式太严苛了，我放弃！
 */
@Slf4j
public class PdfGeneratorIText implements PdfGenerator {


    /**
     * 字体
     */
    private FontProvider fontProvider;

    private volatile static PdfGeneratorIText INSTANCE;


    /**
     * pdf生成器itext
     *
     * @param fontProvider 字体提供者
     */
    public PdfGeneratorIText(FontProvider fontProvider) {
        setFontProvider(fontProvider);
    }

    /**
     * pdf生成器itext
     */
    private PdfGeneratorIText() {
    }


    /**
     * 获得实例
     *
     * @return {@link PdfGenerator}
     */
    public static PdfGenerator getInstance() {
        if (INSTANCE == null) {
            synchronized (PdfGeneratorIText.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PdfGeneratorIText();
                    INSTANCE.initFont();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * 初始化字体
     */
    private void initFont() {
        setFontProvider(new FontProvider());
    }


    /**
     * 设置字体提供者
     *
     * @param fontProvider 字体提供者
     */
    private void setFontProvider(FontProvider fontProvider) {
        this.fontProvider = fontProvider;
        /**
         * 设置中文字体文件的路径，可以在classpath目录下
         */
        fontProvider.addFont(fontPath());
    }


    /**
     * 创建
     *
     * @param html    超文本标记语言
     * @param pdfFile pdf
     *
     * @return boolean
     */
    @Override
    public boolean create(String html, File pdfFile, WatermarkParam... watermark) throws FileNotFoundException {
        pdfFile = FileUtil.touch(pdfFile);

        /**
         * pdfFile也可以是输出流
         */
        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument doc = new PdfDocument(writer);
        doc.setDefaultPageSize(PageSize.A4);
        doc.getDefaultPageSize().applyMargins(20, 20, 20, 20, true);

        /**
         * 获取字体，提供给水印 和 页码使用
         */
        PdfFont pdfFont = fontProvider.getFontSet()
            .getFonts()
            .stream()
            .findFirst()
            .map(fontProvider::getPdfFont)
            .orElse(null);


        /**
         *  水印
         */
        if (ArrayUtil.isNotEmpty(watermark)) {
            doc.addEventHandler(PdfDocumentEvent.END_PAGE, new WaterMarker(pdfFont, watermark));
        }


        /**
         *  页码
         */
        //doc.addEventHandler(PdfDocumentEvent.END_PAGE, new Footer(pdfFont));

        ConverterProperties properties = new ConverterProperties();
        properties.setFontProvider(fontProvider);

        /**
         * PDF目录
         */
        properties.setOutlineHandler(OutlineHandler.createStandardHandler());
        HtmlConverter.convertToPdf(html, doc, properties);

        return true;
    }



}
