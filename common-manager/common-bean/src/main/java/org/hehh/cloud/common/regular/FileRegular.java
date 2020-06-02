package org.hehh.cloud.common.regular;

/**
 * @author : HeHui
 * @date : 2019-04-28 09:44
 * @describe : 文件正则
 */
public class FileRegular {

    /**
     * 视频格式
     */
    public static final String VIDEO = "(wmv|asf|asx|rm|rmvb|mp4|3gp|mov|m4v|avi|dat|mkv|flv|vob|WMV|ASF|ASX|RM|RMVB|MP4|3GP|MOV|M4V|AVI|DAT|MKV|FLV|VOB)";
    /**
     *  音频格式
     */
    public static final String VOICE = "(ACT|REC|VY1|VY2|VY3|VY4|SC4|DVF|MSC|WMA|MP3|WAV|MP3|AVA|WAVE|AMR|act|rec|vy1|vy2|vy3|vy4|sc4|dvf|msc|wma|mp3|wav|mp3|ava|wave|amr)";


    /**
     *  图片
     */
    public static String IMG = ".+(jpg|jpeg|png|bmp|GIF|JPG|PNG|JPEG|pdf|PDF|ico|ICO)$";
}
