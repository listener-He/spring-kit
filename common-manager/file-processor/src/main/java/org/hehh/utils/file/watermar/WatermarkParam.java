package org.hehh.utils.file.watermar;

import lombok.Data;

import java.awt.*;

/**
 * @author: HeHui
 * @date: 2020-09-07 14:10
 * @description: 水印参数
 */
@Data
public class WatermarkParam {

    /**
     * 文本
     */
    private boolean text;

    /**
     * 模式
     */
    private WatermarkMode mode;


    /**
     * src 文字or图片链接
     */
    private String src;


    /**
     * 不透明度
     */
    private float opacity;
    /**
     * 马克角
     */
    private double markAngle;

    /**
     * 颜色
     */
    private Color color;


    /**
     * x坐标
     */
    private Integer margin_x;

    /**
     * y坐标
     */
    private Integer margin_y;

    /**
     * 字体大小
     */
    private Integer fontSize = 20;
}
