package org.hehh.file.upload.res;

import lombok.Data;
import org.hehh.cloud.common.bean.result.Code;
import org.hehh.cloud.common.bean.result.Result;

/**
 * @author: HeHui
 * @date: 2020-08-22 16:43
 * @description: 上传响应
 */
@Data
public class UploadResult<T> extends Result<T> {

    /**
     * 是否存在
     */
    private Boolean existing;


    /**
     * 结果
     *
     * @param data 数据
     *
     * @return {@link UploadResult<T>}
     */
    public static <T> UploadResult<T> result(T data) {
        UploadResult<T> result = new UploadResult<>();
        result.setData(data);
        result.code(Code.OK);
        return result;
    }


}
