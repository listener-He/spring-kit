package org.hehh.utils.file.watermar;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2020-09-04 16:40
 * @description: 水印处理
 */
public interface Watermark {

    /**
     * 文本
     *
     * @param origin      源
     * @param save   保存到
     * @param text      文本
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param color     颜色
     * @return boolean
     * @throws IOException ioexception
     */
    default boolean  text(String origin, String save, String text, int marginX, int marginY, float opacity, double markAngle, Color color, int fontSize) throws IOException {
        return text(new File(origin),new File(save), text,margin_x,margin_y,opacity,markAngle,color,fontSize);
    }

    /**
     * 文本
     *
     * @param origin       origin
     * @param save   保存origin
     * @param text      文本
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @return boolean
     */
    default boolean  text(File origin, File save, String text, int margin_x, int margin_y, float opacity, double markAngle, Color color,int fontSize) throws IOException {
        return text(origin,save,text,margin_x,margin_y,opacity,markAngle, com.blh.common.tool.extra.spring.file.WatermarkMode.SIMP,color,fontSize);
    }


    /**
     * 文本
     *
     * @param origin       origin
     * @param save   保存
     * @param text      文本
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param mode      模式
     * @param color     颜色
     * @param fontSize  字体大小
     * @return boolean
     * @throws IOException ioexception
     */
    boolean  text(File origin, File save, String text, int margin_x, int margin_y, float opacity, double markAngle, com.blh.common.tool.extra.spring.file.WatermarkMode mode, Color color, int fontSize) throws IOException;



    /**
     * 图像
     *
     * @param origin       origin
     * @param save   保存origin
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param img       img
     * @return boolean
     */
    default boolean  image(String origin,String save,String img,int margin_x,int margin_y,float opacity,double markAngle) throws IOException {
        return image(new File(origin),new File(save), new File(img),margin_x,margin_y,opacity,markAngle,null);
    }

    /**
     * 图像
     *
     * @param origin       origin
     * @param save   保存origin
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param img       img
     * @return boolean
     */
    default boolean  image(String origin,String save,String img,int margin_x,int margin_y,float opacity,double markAngle,Color color) throws IOException {
        return image(new File(origin),new File(save), new File(img),margin_x,margin_y,opacity,markAngle,color);
    }


    /**
     * 图像
     *
     * @param origin       origin
     * @param save   保存origin
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param img       img
     * @return boolean
     */
    default boolean  image(File origin,File save,File img,int margin_x,int margin_y,float opacity,double markAngle,Color color) throws IOException {
        return image(origin,save,img,margin_x,margin_y,opacity,markAngle, com.blh.common.tool.extra.spring.file.WatermarkMode.SIMP,color);
    }


    /**
     * 图像
     *
     * @param origin       origin
     * @param save   保存origin
     * @param margin_x  x 轴
     * @param margin_y  y 轴
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param img       img
     * @param mode      模式
     * @return boolean
     */
    boolean  image(File origin, File save, File img, int margin_x, int margin_y, float opacity, double markAngle, com.blh.common.tool.extra.spring.file.WatermarkMode mode, Color color) throws IOException;



    /**
     * 平铺文本
     *
     * @param origin       origin
     * @param save   保存origin
     * @param text      文本
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @return boolean
     */
    default boolean  tiledText(String origin,String save,String text,float opacity,double markAngle,Color color,int fontSize) throws IOException {
        return tiledText(new File(origin),new File(save), text,opacity,markAngle,color,fontSize);
    }


    /**
     * 平铺的文本
     * 平铺文本
     *
     * @param origin       origin
     * @param save   保存origin
     * @param text      文本
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param color     颜色
     * @return boolean
     * @throws IOException ioexception
     */
    default boolean  tiledText(File origin,File save,String text,float opacity,double markAngle,Color color,int fontSize) throws IOException {
        return text(origin,save,text,-1,-1,opacity,markAngle, com.blh.common.tool.extra.spring.file.WatermarkMode.TILED,color,fontSize);
    }


    /**
     * 平铺图像
     *
     * @param origin       origin
     * @param save   保存origin
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param img       img
     * @return boolean
     */
    default boolean  tiledImage(String origin,String save,String img,float opacity,double markAngle) throws IOException {
        return tiledImage(new File(origin),new File(save), new File(img),opacity,markAngle,null);
    }

    /**
     * 平铺图像
     *
     * @param origin       origin
     * @param save   保存origin
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param img       img
     * @return boolean
     */
    default boolean  tiledImage(String origin,String save,String img,float opacity,double markAngle,Color color) throws IOException {
        return tiledImage(new File(origin),new File(save), new File(img),opacity,markAngle,color);
    }


    /**
     * 平铺图像
     *
     * @param origin       origin
     * @param save   保存origin
     * @param opacity   不透明度
     * @param markAngle 水印旋转角度，应在正负45度之间
     * @param img       img
     * @return boolean
     */
    default boolean  tiledImage(File origin,File save,File img,float opacity,double markAngle,Color color) throws IOException {
        return image(origin,save,img,-1,-1,opacity,markAngle, com.blh.common.tool.extra.spring.file.WatermarkMode.TILED,color);
    }
}
