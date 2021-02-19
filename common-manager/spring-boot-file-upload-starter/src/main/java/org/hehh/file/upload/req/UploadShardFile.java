package org.hehh.file.upload.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: HeHui
 * @date: 2021-01-28 16:44
 * @description: 上传分片文件
 */
@Data
public class UploadShardFile extends UploadShardBase {

    /**
     * 文件传输任务ID
     */
    private String taskId;


    /**
     * 分片总数
     */
    private Long totalChunks;

    /**
     * 每个分块的大小
     */
    private Long chunkSize;

    /**
     * 当前分片大小
     */
    private Long currentChunkSize;


    /**
     * 文件名称
     */
    private String filename;


    /**
     * 自动合并
     */
    private Boolean autoMerge = true;

    /**
     * 分块文件传输对象
     */
    private MultipartFile file;


    public Long getCurrentChunkSize() {
        if (currentChunkSize == null) {
            currentChunkSize = file.getSize();
        }
        return currentChunkSize;
    }


}
