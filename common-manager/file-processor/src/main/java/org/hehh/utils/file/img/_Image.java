package org.hehh.utils.file.img;


import java.awt.*;

/**
 * @author: HeHui
 * @date: 2020-06-04 17:15
 * @description: 图片
 */
public interface _Image {


    /**
     *  图片
     * @return
     */
    Image img();

    /**
     *
     *  文件名
     */
    String fileName();


    /**
     *  宽度
     */
    int width();


    /**
     *  高度
     */
    int high();


    /**
     *  大小
     */
    long size();



}
