package org.hehh.ffmpeg;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;

/**
 * @author : HeHui
 * @date : 2019-03-29 13:08
 * @describe : ffmpeg 工具类
 */
public class FfmpegKit {


    /**ffmpeg 安装目录*/
    private static String ffmpeg = "ffmpeg";


    public static void setFfmpegPath(String ffmpeg){
        FfmpegKit.ffmpeg = ffmpeg;
    }



    public static String getFfmpegPath(){

        if(StrUtil.isNotBlank(ffmpeg)){
            return ffmpeg;
        }
        OsInfo osInfo = SystemUtil.getOsInfo();

        if(osInfo.isWindows()){
            ffmpeg = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\jave-1\\ffmpeg";
        }

       assert StrUtil.isNotBlank(ffmpeg):"The current system not installation ffmpeg";
       return ffmpeg;
    }













    /**
     *   音频转MP3
     *       特别注意暂只支持 liunx 和 windows
     *          其中windows下的ffmpeg安装目录需根据自己的安装目录配置
     *          目前使用默认路径
     * @param file
     * @return
     */
    public static String  toMp3(String file){
        if(StrUtil.isBlank(file) || !FileUtil.isFile(file)){
            return null;
        }
        String shell = getFfmpegPath()+" -i {} -acodec libmp3lame {}";

        String parent = FileUtil.getParent(file, 1);
        String baseName = FileUtil.getName(file);
        String s = parent + "/" + baseName + ".mp3";
        try {
            Process exec = RuntimeUtil.exec(StrUtil.format(shell, file, s));
            if(exec != null){
                String result = RuntimeUtil.getResult(exec);

                /** 暂时也只能用这种方案来判断是否成功了*/
                if(StrUtil.startWith(result,"FFmpeg version")){
                    return s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return s;
    }




    /**
     *  压缩视频文件
     * @param videoFile 源文件
     * @param frame 表示1秒多少帧
     * @param audio 表示音频的码率
     * @param compressionVideoFile 转换后的文件
     * @return
     */
    public static String compressionVideo(String videoFile,int frame,int audio,String compressionVideoFile){

        /**验证源文件*/
        if(!FileUtil.isFile(videoFile)){
            return null;
        }

        /**
         * 验证转换为的文件
         */
        if(StrUtil.isBlank(compressionVideoFile)){
            compressionVideoFile = videoFile.substring(0,videoFile.lastIndexOf("."))+"-compression"+videoFile.substring(videoFile.indexOf("."));
        }

        /**验证源文件*/
        assert FileUtil.isFile(compressionVideoFile):"转换文件compressionVideoFile 不是一个文件";

        String shell = getFfmpegPath()+" -i {} -r {} -b:a {} {}";

        try {
            Process exec = RuntimeUtil.exec(StrUtil.format(shell, videoFile,frame,audio+"k",compressionVideoFile));
            if(exec != null){
                RuntimeUtil.getResult(exec);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return compressionVideoFile;
    }
}
