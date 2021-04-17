package org.hehh.file.upload.req;

import lombok.Data;

/**
 * @author: HeHui
 * @date: 2021-02-19 11:24
 * @description: 上传分片基础
 */
@Data
public class UploadShardBase extends UploadBase {

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 文件总大小
     */
    private Long totalSize;

    /**
     * 当前为第几分片
     */
    private Long chunkNumber;
}
