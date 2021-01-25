package org.hehh.utils.file.office;

import org.hehh.utils.StrKit;

import java.io.InputStream;

/**
 * @author: HeHui
 * @date: 2021-01-25 16:35
 * @description: office类型
 */
public class OfficeMedia {


    /**
     * 类型
     *
     * @author hehui
     * @date 2021/01/25
     */
    public static enum Type {
        xls("xls"), xlsx("xlsx"),
        doc("doc"), docx("docx"),
        ppt("ppt"), pptx("pptx"),
        vsd("vsd"), vsdx("vsdx");

        String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }



    /**
     * 判断office文件的具体类型
     *
     * @param fileInputStream office文件的进一步解析
     * @return office文件具体类型
     */
    public static Type getType(InputStream fileInputStream) {
        byte[] b = new byte[512];
        try {
            fileInputStream.read(b, 0, b.length);
            String fileTypeHex = StrKit.hexStr(b);
            String flagString = fileTypeHex.substring(992);
            if (flagString.toLowerCase().startsWith("eca5c")) {
                return Type.doc;
            } else if (flagString.toLowerCase().startsWith("fdffffff09")) {
                return Type.xls;
            } else if (flagString.toLowerCase().startsWith("09081000000")) {
                return Type.xls;
            } else {
                return Type.ppt;
            }
        } catch (Exception exception) {
            return null;
        }
    }


}
