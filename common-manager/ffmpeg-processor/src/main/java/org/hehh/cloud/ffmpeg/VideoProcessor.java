package org.hehh.cloud.ffmpeg;

import  org.bytedeco.javacpp.avcodec.*;
import  org.bytedeco.javacpp.avformat.*;
import  org.bytedeco.javacpp.avutil.*;
import  org.bytedeco.javacpp.swscale.*;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.avformat;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileSystemNotFoundException;

/**
 * @author: HeHui
 * @date: 2020-06-02 15:17
 * @description: 视频处理器
 */
public class VideoProcessor {

    private final FfmpegGlobal global;



    public VideoProcessor(FfmpegGlobal global){
        this.global = global;
    }












}
