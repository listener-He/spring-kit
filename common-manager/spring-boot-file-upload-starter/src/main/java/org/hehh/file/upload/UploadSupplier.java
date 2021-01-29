package org.hehh.file.upload;

import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2021-01-29 15:52
 * @description: 上传结果提供者
 */
@FunctionalInterface
public interface UploadSupplier<T> {

    /**
     * 上传
     *
     * @return {@link T}
     *
     * @throws IOException ioexception
     */
    T upload() throws IOException;
}
