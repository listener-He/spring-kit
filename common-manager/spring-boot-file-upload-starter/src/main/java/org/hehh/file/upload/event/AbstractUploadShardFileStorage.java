package org.hehh.file.upload.event;

import org.hehh.file.upload.UploadShardFileStorage;
import org.hehh.file.upload.req.UploadBase;
import org.hehh.file.upload.req.UploadShardFile;
import org.hehh.file.upload.res.UploadShardFileResult;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

/**
 * @author: HeHui
 * @date: 2021-01-28 17:01
 * @description: 上传分片文件
 */
public abstract class AbstractUploadShardFileStorage implements UploadShardFileStorage {

    private final String[] defaultTypes;

    private UploadFilterChain uploadFilterChain;

    /**
     * 摘要上传碎片文件存储
     *
     * @param defaultTypes 默认类型
     */
    public AbstractUploadShardFileStorage(String... defaultTypes) {
        this.defaultTypes = defaultTypes;
    }

    /**
     * 摘要上传碎片文件存储
     *
     * @param defaultTypes      默认类型
     * @param uploadFilterChain 上传过滤器链
     */
    public AbstractUploadShardFileStorage(UploadFilterChain uploadFilterChain, String... defaultTypes) {
        this.defaultTypes = defaultTypes;
        this.uploadFilterChain = uploadFilterChain;
    }

    /**
     * 摘要上传碎片文件存储
     *
     * @param uploadFilterChain 上传过滤器链
     */
    public AbstractUploadShardFileStorage(UploadFilterChain uploadFilterChain) {
        this();
        this.uploadFilterChain = uploadFilterChain;
    }


    /**
     * 摘要上传碎片文件存储
     */
    public AbstractUploadShardFileStorage() {
        this.defaultTypes = null;
    }

    /**
     * 上传
     *
     * @param shardFile 碎片文件
     * @param directory 目录
     */
    @Override
    public UploadShardFileResult upload(UploadShardFile shardFile, String directory) throws FileNotFoundException {
        MultipartFile file = shardFile.getFile();

        if (file == null || file.isEmpty()) {
            throw new FileNotFoundException("上传文件为空");
        }

        UploadEvent event = new UploadEvent(shardFile);

        if (uploadFilterChain == null) {
            //TODO 执行保存操作
            return null;
        }

        uploadFilterChain.before(event);
        if (event.getCompleted()) {
            return UploadShardFileResult.existing(event.getEvent(UploadBase.class).getUrl());
        }


        String url = null;
        try {
            //TODO 执行保存操作
            url = null;
            uploadFilterChain.after(event, url);
            return UploadShardFileResult.result();
        } catch (Exception e) {
            if (!StringUtils.isEmpty(url)) {
                event.setCompleted(true);
            }
            uploadFilterChain.error(event, e);
            url = event.getEvent(UploadBase.class).getUrl();

            if (!StringUtils.isEmpty(url)) {
                return UploadShardFileResult.result(url);
            }
            throw e;
        }
    }


}
