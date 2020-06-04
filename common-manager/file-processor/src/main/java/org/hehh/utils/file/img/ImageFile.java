package org.hehh.utils.file.img;


import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2020-06-04 16:29
 * @description: 图片文件
 */
public class ImageFile  implements _Image {



    /**
     *  图片
     */
    private final Image img;


    /**
     *
     *  文件名
     */
    private final String fileName;


    /**
     *  宽度
     */
    private final int width;


    /**
     *  高度
     */
    private final int high;


    /**
     *  大小
     */
    private final long size;


    /**
     *  构造器1
     * @param filePath 图片路径
     * @throws IOException
     */
    public ImageFile(String filePath) throws IOException {
        this(new File(filePath));
    }



    /**
     *  构造器2
     * @param file 文件
     * @throws IOException
     */
    public ImageFile(File file) throws IOException {
        this(Toolkit.getDefaultToolkit().getImage(file.getCanonicalPath()),file.length(),file.getName());
    }



    /**
     *   构造器3
     * @param file 图片
     * @param size 文件大小
     */
    public ImageFile(Image file,long size,String name){
        assert file != null : "I don't think it's a picture";
        this.img = file;
        this.width = this.img.getWidth(null);
        this.high = this.img.getHeight(null);
        this.size = size;
        this.fileName = name;
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
