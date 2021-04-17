package org.hehh.file.upload.res;

import lombok.Data;
import org.hehh.cloud.common.bean.result.Code;

/**
 * @author: HeHui
 * @date: 2021-01-29 13:47
 * @description: 分片上传响应
 */
@Data
public class UploadShardFileResult extends UploadResult<String> {



    /**
     * 上传的API
     */
    private String uploadUrl;


    /**
     * 现有的
     *
     * @param url url
     *
     * @return {@link UploadShardFileResult}
     */
    public static UploadShardFileResult existing(String url) {
        UploadShardFileResult result = result();
        result.setExisting(true);
        result.setData(url);
        return result;
    }

    /**
     * 结果
     *
     * @return {@link UploadShardFileResult}
     */
    public static UploadShardFileResult result() {
        UploadShardFileResult result = new UploadShardFileResult();
        result.code(Code.OK);
        return result;
    }

    /**
     * 结果
     *
     * @param url url
     *
     * @return {@link UploadShardFileResult}
     */
    public static UploadShardFileResult result(String url) {
        UploadShardFileResult result = result();
        result.setExisting(true);
        result.setData(url);
        return result;
    }
}
