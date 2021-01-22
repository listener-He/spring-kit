package org.hehh.utils.file.pdf.event;


import cn.hutool.core.collection.CollUtil;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: HeHui
 * @date: 2020-09-07 12:30
 * @description: 页码实现
 */
public class Footer implements IEventHandler {

    private PdfFont pdfFont;

    private List<String> text;

    protected PdfFormXObject placeholder; // 相对坐标系


    /**
     * 页脚
     *
     * @param font 字体
     * @param text 文本
     */
    public Footer(PdfFont font, String... text) {
        this(font, Arrays.asList(text));
    }

    /**
     * 页脚
     *
     * @param font 字体
     * @param text 文本
     */
    public Footer(PdfFont font, List<String> text) {
        assert font != null : "字体不能为空";
        assert !CollUtil.isEmpty(text) : "页脚内容不能为空";
        this.pdfFont = font;
        this.text = text;
        placeholder = new PdfFormXObject(new Rectangle(0, 0, 500, 78));
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
            page.getLastContentStream(), page.getResources(), pdf);
        pdfCanvas.addXObject(placeholder, 92, 50);
        Canvas canvas = new Canvas(pdfCanvas, pageSize, true);


        try {
            canvas.setFont(pdfFont);
            canvas.setFontSize(7.5f);
            AtomicInteger tCount = new AtomicInteger(0);
            text.forEach(v -> {

                int y = tCount.addAndGet(Float.valueOf(pdfFont.getWidth(v, 7.5f)).intValue());
                canvas.showTextAligned(v, 0, y, TextAlignment.LEFT);

            });

        } finally {
            canvas.close();
        }

        pdfCanvas.release();

    }
}
