package org.hehh.cloud.ffmpeg;

import cn.hutool.core.util.StrUtil;

/**
 * @author: HeHui
 * @date: 2020-06-02 15:15
 * @description: 全局配置
 */
public class FfmpegGlobal {

    private  final String ffmpeg;



    public FfmpegGlobal(String ffmpeg){
        assert StrUtil.isNotBlank(ffmpeg) : "ffmpeg命令空间不能为空!";
        this.ffmpeg = ffmpeg;
    }



}
