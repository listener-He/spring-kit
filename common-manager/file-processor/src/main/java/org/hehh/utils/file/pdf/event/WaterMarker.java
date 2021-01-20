package org.hehh.utils.file.pdf.event;

import com.blh.common.tool.extra.spring.file.WatermarkMode;
import com.blh.common.tool.extra.spring.file.WatermarkParam;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author: HeHui
 * @date: 2020-09-07 14:19
 * @description: 水印事件处理
 */
public class WaterMarker implements IEventHandler {

    private final PdfFont font;

    private final List<WatermarkParam> watermark;

    public WaterMarker(PdfFont font, List<WatermarkParam> watermark) {
        assert font != null : "字体不能为空";
        assert watermark != null && !watermark.isEmpty() : "水印不能为空";
        this.font = font;
        this.watermark = watermark;
    }

    public WaterMarker(PdfFont font, WatermarkParam... watermark) {
        this(font, Arrays.asList(watermark));
    }



    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
            page.getLastContentStream(), page.getResources(), pdf);


        Canvas canvas = new Canvas(pdfCanvas, pageSize,false);

        float pageHeight = pageSize.getHeight();
        float pageWidth = pageSize.getWidth();

        /**
         *  间隙
         */
        int interval = -5;

        watermark.stream().filter(v-> v.isText()).forEach(x->{
            Paragraph waterMarker = new Paragraph(x.getSrc())
                .setFont(font)
                .setOpacity(x.getOpacity())
                .setFontSize(x.getFontSize())
                .setFontColor(new DeviceRgb(x.getColor()));


            if(x.getMode().equals(WatermarkMode.TILED)){

                int textW = Float.valueOf(font.getWidth(x.getSrc(), x.getFontSize())).intValue();
                int textH = font.getAscent(x.getSrc(), x.getFontSize());

                /**
                 * 根据纸张大小多次添加， 水印文字成度角倾斜
                 */
                for (int height = interval + textH; height < pageHeight; height = height + textH * 3) {
                    for (int width = interval + textW; width < pageWidth + textW; width = width + textW * 2) {
                        canvas.showTextAligned(waterMarker,width - textW, height - textH,pdf.getNumberOfPages(),TextAlignment.LEFT,VerticalAlignment.BOTTOM,Double.valueOf(x.getMarkAngle()).floatValue());
                    }
                }
            }else{
                canvas.showTextAligned(waterMarker,x.getMargin_x(),x.getMargin_y(),pdf.getNumberOfPages(),TextAlignment.CENTER,VerticalAlignment.BOTTOM,Double.valueOf(x.getMarkAngle()).floatValue());
            }
        });

        watermark.stream().filter(v-> !v.isText()).forEach(x->{
            try {
                ImageData image = ImageDataFactory.create(new ImageIcon(x.getSrc()).getImage(),x.getColor());
                image.setRotation(Double.valueOf(x.getMarkAngle()).floatValue());
                if(x.getMode().equals(WatermarkMode.SIMP)){
                    pdfCanvas.addImage(image,x.getMargin_x(),x.getMargin_y(),false);
                }else if(x.getMode().equals(WatermarkMode.TILED)){
                    int textW = Float.valueOf(image.getWidth()).intValue();
                    int textH = Float.valueOf(image.getHeight()).intValue();
                    /**
                     * 根据纸张大小多次添加， 水印文字成度角倾斜
                     */
                    for (int height = interval + textH; height < pageHeight; height = height + textH * 3) {
                        for (int width = interval + textW; width < pageWidth + textW; width = width + textW * 2) {
                            pdfCanvas.addImage(image, width - textW, height - textH, false);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        canvas.close();
        pdfCanvas.release();
    }
}
