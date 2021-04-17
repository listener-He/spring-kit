package org.hehh.utils.file.pdf;

import cn.hutool.core.io.FileUtil;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.hehh.utils.file.watermar.WatermarkMode;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * @author: HeHui
 * @date: 2020-09-04 11:17
 * @description: pdf水印处理 itext 方式处理
 */
@Slf4j
public class PDFWatermarkItext implements PDFWatermark {



    /**
     * 文本
     *
     * @param pdf       pdf
     * @param savePdf   保存pdf
     * @param text      文本
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param mode      模式
     *
     * @return boolean
     */
    @Override
    public boolean text(File pdf, File savePdf, String text, int margin_x, int margin_y, float opacity, double markAngle, WatermarkMode mode, Color color, int fontSize) throws IOException {
        /**
         * 要输出的pdf文件(待添加水印)
         */
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savePdf));

        PdfReader reader = new PdfReader(FileUtil.getCanonicalPath(pdf));
        PdfStamper stamper = null;
        try {
            stamper = new PdfStamper(reader, bos);

            /**
             *  pdf总页数
             */
            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content;

            BaseFont base = BaseFont.createFont();

            /**
             * 间隔,获取水印文字的高度和宽度
             */
            int interval = -5;
            int textH = 0, textW = 0;

            if (mode.equals(WatermarkMode.TILED)) {
                JLabel label = new JLabel();
                label.setText(text);
                FontMetrics metrics = label.getFontMetrics(label.getFont());
                textH = metrics.getHeight();
                textW = metrics.stringWidth(label.getText());
            }


            /**
             * 设置水印透明度
             */
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(opacity);
            gs.setStrokeOpacity(opacity);

            for (int i = 1; i < total; i++) {
                /**
                 * 在内容上方加水印
                 */
                content = stamper.getOverContent(i);
                /**
                 * 在内容下方加水印
                 * content = stamper.getUnderContent(i);
                 */

                content.saveState();
                content.setGState(gs);

                content.beginText();


                /**
                 *  字体大小 颜色
                 */
                content.setColorFill(color);
                content.setFontAndSize(base, fontSize);


                switch (mode) {
                    case SIMP:
                        contentSimpText(text, margin_x, margin_y, markAngle, content, Element.ALIGN_CENTER);
                        break;
                    case TILED:
                        contentTiledText(text, markAngle, reader, content, interval, textH, textW, i);
                        break;
                    default:
                        throw new RuntimeException("该模式还未实现");
                }

                content.endText();
            }
            return true;

        } catch (DocumentException e) {
            log.error("new PdfStamper异常:", e);
            return false;
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                reader.close();
            }

        }
    }

    /**
     * 笨人的文本内容
     *
     * @param text        文本
     * @param margin_x    保证金x
     * @param margin_y    保证金y
     * @param markAngle   马克角
     * @param content     内容
     * @param alignCenter 居中对齐
     */
    private void contentSimpText(String text, int margin_x, int margin_y, double markAngle, PdfContentByte content, int alignCenter) {
        /**
         *  文本对齐方式
         */
        content.showTextAligned(alignCenter, text, margin_x, margin_y, Double.valueOf(markAngle).intValue());
    }


    /**
     * 内容水平铺满文本
     *
     * @param text      文本
     * @param markAngle 马克角
     * @param reader    读者
     * @param content   内容
     * @param interval  时间间隔
     * @param textH     texth
     * @param textW     textw
     * @param i         我
     */
    private void contentTiledText(String text, double markAngle, PdfReader reader, PdfContentByte content, int interval, int textH, int textW, int i) {
        /**
         * 获取每一页的高度、宽度
         */

        Rectangle pageSizeWithRotation = reader.getPageSizeWithRotation(i);
        float pageHeight = pageSizeWithRotation.getHeight();
        float pageWidth = pageSizeWithRotation.getWidth();
        /**
         * 根据纸张大小多次添加， 水印文字成度角倾斜
         */
        for (int height = interval + textH; height < pageHeight; height = height + textH * 3) {
            for (int width = interval + textW; width < pageWidth + textW; width = width + textW * 2) {
                content.showTextAligned(Element.ALIGN_LEFT, text, width - textW, height - textH, Double.valueOf(markAngle).intValue());
            }
        }
    }


    /**
     * 图像
     *
     * @param pdf       pdf
     * @param savePdf   保存pdf
     * @param img       img
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param mode      模式
     *
     * @return boolean
     */
    @Override
    public boolean image(File pdf, File savePdf, File img, int margin_x, int margin_y, float opacity, double markAngle, WatermarkMode mode, Color color) throws IOException {
        /**
         * 要输出的pdf文件(待添加水印)
         */
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savePdf));

        PdfReader reader = new PdfReader(FileUtil.getCanonicalPath(pdf));
        PdfStamper stamper = null;
        try {
            stamper = new PdfStamper(reader, bos);

            /**
             *  pdf总页数
             */
            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content;


            /**
             * 间隔,获取水印文字的高度和宽度
             */
            int interval = -5;
            int textH = 0, textW = 0;
            Image image = new ImageIcon(img.getCanonicalPath()).getImage();
            if (mode.equals(WatermarkMode.TILED)) {
                textW = image.getWidth(null);
                textH = image.getHeight(null);
                assert textH > 0 && textW > 0 : "水印文件不是一个图片";
            }


            /**
             * 设置水印透明度
             */
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(opacity);
            gs.setStrokeOpacity(opacity);


            for (int i = 1; i < total; i++) {


                /**
                 * 在内容下方加水印
                 *
                 */
                content = stamper.getOverContent(i);

                content.saveState();
                content.setGState(gs);
                content.beginText();


                switch (mode) {
                    case SIMP:
                        contentImg(image, markAngle, content, margin_x, margin_y, color);
                        break;
                    case TILED:
                        /**
                         * 获取每一页的高度、宽度
                         */

                        Rectangle pageSizeWithRotation = reader.getPageSizeWithRotation(i);
                        float pageHeight = pageSizeWithRotation.getHeight();
                        float pageWidth = pageSizeWithRotation.getWidth();
                        /**
                         * 根据纸张大小多次添加， 水印图谱成度角倾斜
                         */
                        for (int height = interval + textH; height < pageHeight; height = height + textH * 3) {
                            for (int width = interval + textW; width < pageWidth + textW; width = width + textW * 2) {
                                contentImg(image, markAngle, content, height - textH, width - textW, color);
                            }
                        }
                        break;
                    default:
                        throw new RuntimeException("该模式还未实现");
                }

                content.endText();
            }
            return true;

        } catch (DocumentException e) {
            log.error("new PdfStamper异常:", e);
            return false;
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                reader.close();
            }

        }
    }

    /**
     * 内容img
     *
     * @param img       img
     * @param markAngle 马克角
     * @param content   内容
     * @param height    高度
     * @param width     宽度
     * @param color
     *
     * @throws IOException       ioexception
     * @throws DocumentException 文件异常
     */
    private void contentImg(Image img, double markAngle, PdfContentByte content, int height, int width, Color color) throws IOException, DocumentException {
        com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(img, color);

        /**
         * 设置坐标
         */
        image.setAbsolutePosition(width, height);
        /**
         *  设置☑旋转
         */
        image.setRotation(Double.valueOf(markAngle).floatValue());
        content.addImage(image);
    }


}
