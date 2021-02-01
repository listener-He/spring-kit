package org.hehh.file.upload;

import org.hehh.file.upload.req.UploadShardFile;
import org.hehh.file.upload.res.UploadShardFileResult;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2021-01-28 16:42
 * @description: 上传分片文件存储
 */
public interface UploadShardFileStorage {


    /**
     * 上传
     *
     * @param shardFile 碎片文件
     * @param directory 目录
     */
    UploadShardFileResult upload(UploadShardFile shardFile, String directory) throws IOException;


    /**
     * 上传
     *
     * @param shardFile 碎片文件
     */
    void upload(UploadShardFile shardFile);

}
