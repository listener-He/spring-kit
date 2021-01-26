package org.hehh.file.upload;

import org.hehh.file.upload.event.UploadFileEvent;
import org.hehh.file.upload.exception.UploadFileException;
import org.hehh.utils.file.FileUtil;
import org.hehh.utils.file.pojo.FileMedia;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author: HeHui
 * @date: 2021-01-26 11:11
 * @description: 抽象上传文件存储（事件驱动）
 */
public abstract class AbstractUploadFileStorage implements UploadFileStorage {

    private final String[] defaultTypes;

    private UploadFileEvent event;

    public AbstractUploadFileStorage(String... defaultTypes) {
        this.defaultTypes = defaultTypes;
    }

    public AbstractUploadFileStorage() {
        this.defaultTypes = null;
    }

    /**
     * 验证类型
     *
     * @param file  文件
     * @param types 类型
     *
     * @throws IOException ioexception
     */
    protected void verifyType(MultipartFile file, String... types) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (types == null && defaultTypes != null) {
                types = defaultTypes;
            }
            if (types != null) {
                String type = FileMedia.getType(file.getInputStream());
                if (!StringUtils.isEmpty(type)) {
                    if (Arrays.stream(types).anyMatch(v -> v.equalsIgnoreCase(type))) {
                        return;
                    }
                    throw new UploadFileException(String.format("文件声明类型不匹配,s%,上传类型: s%,需要类型: s%", file.getOriginalFilename(), type, types));
                } else {
                    throw new UploadFileException(String.format("文件无声明类型,s%,size: s%", file.getOriginalFilename(), file.getSize()));
                }
            }
            return;
        }
        throw new FileNotFoundException("上传文件为空");
    }

    /**
     * 上传
     *
     * @param fileId    文件标识
     * @param file      文件
     * @param filename  文件名
     * @param directory 目录
     *
     * @return {@link String}
     *
     * @throws IOException ioexception
     */
    protected abstract String upload(String fileId, MultipartFile file, String filename, String directory) throws IOException;


    /**
     * 上传 (核心方法，所有上传基于这个方法)
     *
     * @param file      文件
     * @param filename  文件名
     * @param directory 目录
     *
     * @return {@link String}
     *
     * @throws IOException 文件未发现异常
     */
    @Override
    public String upload(MultipartFile file, String filename, String directory) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new FileNotFoundException("上传文件为空");
        }

        /**
         *  获取文件ID
         */
        String fileId = FileUtil.md5(file.getInputStream());


        event.before(fileId, file.getOriginalFilename());

        try {
            /**
             *  如果文件名为空就用hash命名
             */
            if (StringUtils.isEmpty(filename)) {
                filename = fileId + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }
            String url = upload(fileId, file, filename, directory);

            event.after(fileId, url);
            return url;
        } catch (Exception e) {
            event.error(fileId, e);
            throw e;
        }
    }

    /**
     * 上传
     *
     * @param file      文件
     * @param filename  文件名
     * @param directory 目录
     * @param types     类型
     *
     * @return {@link String}
     *
     * @throws IOException 文件未发现异常
     */
    @Override
    public String upload(MultipartFile file, String filename, String directory, String... types) throws IOException {
        verifyType(file, types);
        return upload(file, filename, directory);
    }
}
