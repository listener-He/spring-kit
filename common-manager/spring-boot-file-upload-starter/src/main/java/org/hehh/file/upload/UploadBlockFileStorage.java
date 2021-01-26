package org.hehh.file.upload;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: HeHui
 * @date: 2020-08-22 16:25
 * @description: 分块上传文件存储
 */
public interface UploadBlockFileStorage extends UploadFileStorage {


    /**
     * 是否上传完成
     *
     * @param key 关键
     *
     * @return boolean
     */
    boolean isUploaded(String key);


    default Void uploadBlock(String key, MultipartFile file, long targetSize, long inputStreamSize, int chunks, int chunk) {
        return this.uploadBlock(key, file, targetSize, inputStreamSize, chunks, chunk, null);
    }


    default Void uploadBlock(String key, MultipartFile file, long targetSize, long inputStreamSize, int chunks, int chunk, String filename) {
        return this.uploadBlock(key, file, targetSize, inputStreamSize, chunks, chunk, filename, null);
    }


    Void uploadBlock(String key, MultipartFile file, long targetSize, long inputStreamSize, int chunks, int chunk, String filename, String directory);
}
