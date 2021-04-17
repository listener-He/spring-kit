package org.hehh.utils.file.pojo;

import cn.hutool.core.io.FileTypeUtil;
import org.hehh.utils.StrKit;
import org.hehh.utils.file.FileUtil;

import java.io.*;
import java.util.*;

/**
 * @author: HeHui
 * @date: 2021-01-25 15:58
 * @description: 文件类型
 */
public class FileMedia {

//    private final static Map<String, String> FILE_TYPE = new HashMap<>();

    /**-----------------------------目前可以识别的类型----------------------------*/
//    static {
//        /**---------more type:  http://blog.csdn.net/holandstone/article/details/7624343 ----------*/
//        FILE_TYPE.put("jpg", "FFD8FF"); //JPEG
//        FILE_TYPE.put("png", "89504E47"); //PNG
//        FILE_TYPE.put("gif", "47494638"); //GIF
//        FILE_TYPE.put("tif", "49492A00"); //TIFF
//        FILE_TYPE.put("bmp", "424D"); //Windows Bitmap
//        FILE_TYPE.put("dwg", "41433130"); //CAD
//        FILE_TYPE.put("rtf", "7B5C727466"); //Rich Text Format
//        FILE_TYPE.put("pdf", "255044462D312E"); //Adobe Acrobat
//        FILE_TYPE.put("xml", "3C3F786D6C");
//        FILE_TYPE.put("zip", "504B0304");
//        FILE_TYPE.put("mid", "504B0304");
//        FILE_TYPE.put("rar", "52617221");
//        FILE_TYPE.put("html", "68746D6C3E"); //HTML
//        FILE_TYPE.put("psd", "38425053"); //PhotoShop
//        FILE_TYPE.put("eml", "44656C69766572792D646174653A"); //Email [thorough only]
//        FILE_TYPE.put("dbx", "CFAD12FEC5FD746F"); //Outlook Express
//        FILE_TYPE.put("pst", "2142444E"); //Outlook
//        FILE_TYPE.put("office", "D0CF11E0"); //office类型，包括doc、xls和ppt
//        FILE_TYPE.put("mdb", "000100005374616E64617264204A"); //MS Access
//        FILE_TYPE.put("wpd", "FF575043"); //WordPerfect
//        FILE_TYPE.put("eps", "252150532D41646F6265");
//        FILE_TYPE.put("ps", "252150532D41646F6265");
//        FILE_TYPE.put("qdf", "AC9EBD8F"); //Quicken
//        FILE_TYPE.put("pwl", "E3828596"); //Windows Password
//        FILE_TYPE.put("wav", "57415645"); //Wave
//        FILE_TYPE.put("avi", "41564920");
//        FILE_TYPE.put("ram", "2E7261FD"); //Real Audio
//        FILE_TYPE.put("rm", "2E524D46"); //Real Media
//        FILE_TYPE.put("mpg", "000001BA"); //
//        FILE_TYPE.put("mov", "6D6F6F76"); //Quicktime
//        FILE_TYPE.put("asf", "3026B2758E66CF11"); //Windows Media
//        FILE_TYPE.put("jif", "52617221");
//    }


    /**
     * 得到类型
     *
     * @param file 文件
     *
     * @return {@link String}
     */
    public static String getType(File file) {
        return FileUtil.getType(file);
    }

    /**
     * 得到类型
     *
     * @param stream 流
     *
     * @return {@link String}
     */
    public static String getType(InputStream stream) {
        return FileTypeUtil.getType(stream);
//        String header = getHeader(stream);
//        if (StrUtil.isNotBlank(header)) {
//            Optional<String> optional = FILE_TYPE.entrySet().stream()
//                .filter(entry -> header.toUpperCase().startsWith(entry.getValue()))
//                .map(Map.Entry::getKey).findFirst();
//            if (optional.isPresent()) {
//
//                String type = optional.get();
//                if (type.equals("office")) {
//                    OfficeMedia.Type officeMedia = OfficeMedia.getType(stream);
//                    if (officeMedia != null) {
//                        type = officeMedia.getName();
//                    }
//                }
//                return type;
//            }
//        }

       // return null;
    }


    /**
     * 得到文件头
     *
     * @param file 文件
     *
     * @return {@link String}
     */
    private String getFileHeader(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            try {
                return getHeader(stream);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 得到文件头
     * 不关闭流
     *
     * @param inputStream 文件
     *
     * @return {@link String}
     */
    private String getHeader(InputStream inputStream) {
        /**
         * 读取字节长度
         */
        byte[] b = new byte[28];

        try {
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return StrKit.hexStr(b);
    }


}
