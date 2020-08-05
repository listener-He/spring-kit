package org.hehh.utils.file.img;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author: HeHui
 * @date: 2020-06-04 16:29
 * @description: 图片文件
 */
public class ImageFile extends File  implements _Image {


    /**
     *  图片
     */
    private  Image img;


    /**
     *
     *  文件名
     */
    private  String fileName;


    /**
     *  宽度
     */
    private  int width;


    /**
     *  高度
     */
    private  int high;


    /**
     *  大小
     */
    private  long size;




    private ImageFile(String parent, String child) {
        super(parent, child);
    }


    private ImageFile(File parent, String child) {
        super(parent, child);
    }


    private ImageFile(URI uri) {
        super(uri);
    }





    /**
     *  静态构建
     * @param filePath
     * @return
     * @throws IOException
     */
    public static ImageFile of(String filePath) throws IOException {
        assert filePath != null : "I don't think it's a picture";
        return new ImageFile(filePath);
    }




    /**
     *  构造器1
     * @param filePath 图片路径
     * @throws IOException
     */
    public ImageFile(String filePath) throws IOException {
        super(filePath);


        this.img = Toolkit.getDefaultToolkit().getImage(super.getCanonicalPath());
        this.width = this.img.getWidth(null);
        this.high = this.img.getHeight(null);
        this.size = super.length();
        this.fileName = super.getName();
    }



    /**
     * 文件名
     */
    @Override
    public String fileName() {
        return fileName;
    }



    /**
     * 宽度
     */
    @Override
    public int width() {
        return width;
    }



    /**
     * 高度
     */
    @Override
    public int high() {
        return high;
    }



    /**
     * 大小
     */
    @Override
    public long size() {
        return size;
    }



    /**
     * 图片
     *
     * @return
     */
    @Override
    public Image img() {
        return img;
    }



}
