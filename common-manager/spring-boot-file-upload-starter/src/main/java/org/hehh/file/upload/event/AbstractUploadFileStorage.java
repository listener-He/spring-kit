package org.hehh.file.upload.event;

import lombok.extern.slf4j.Slf4j;
import org.hehh.file.upload.UploadFileStorage;
import org.hehh.file.upload.exception.UploadFileException;
import org.hehh.file.upload.req.UploadMultipartFile;
import org.hehh.file.upload.res.UploadResult;
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
@Slf4j
public abstract class AbstractUploadFileStorage implements UploadFileStorage {

    private final String[] defaultTypes;

    private UploadFilterChain uploadFilterChain;

    /**
     * 抽象上传文件存储
     *
     * @param defaultTypes 默认类型
     */
    public AbstractUploadFileStorage(String... defaultTypes) {
        this.defaultTypes = defaultTypes;
    }

    /**
     * 摘要上传文件存储
     *
     * @param defaultTypes      默认类型
     * @param uploadFilterChain 上传过滤器链
     */
    public AbstractUploadFileStorage(UploadFilterChain uploadFilterChain, String... defaultTypes) {
        this.defaultTypes = defaultTypes;
        this.uploadFilterChain = uploadFilterChain;
    }

    /**
     * 摘要上传文件存储
     *
     * @param uploadFilterChain 上传过滤器链
     */
    public AbstractUploadFileStorage(UploadFilterChain uploadFilterChain) {
        this();
        this.uploadFilterChain = uploadFilterChain;
    }


    public AbstractUploadFileStorage() {
        this.defaultTypes = null;
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
     * 上传
     *
     * @param req       请求参数
     * @param directory 目录
     * @param types     类型
     *
     * @return {@link UploadResult<String>}
     */
    @Override
    public UploadResult<String> upload(UploadMultipartFile req, String directory, String... types) throws IOException {
        MultipartFile file = req.getFile();

        if (file == null || file.isEmpty()) {
            throw new FileNotFoundException("上传文件为空");
        }
        /**获取文件真实类型*/
        String type = getType(file);

        /**验证文件*/
        verifyType(file, type, types);

        /**文件hash获取文件ID*/
        String fileId = FileUtil.md5(file.getInputStream());


        /**上传文件提交类型*/
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);


        if (!type.equalsIgnoreCase(fileType)) {
            log.warn("上传文件类型与真实类型不符合,请注意审查！上传文件:{},真实类型:{}", file.getOriginalFilename(), type);
        }

        String filename = req.getFilename();
        if (StringUtils.isEmpty(filename)) {
            /**
             *  如果文件名为空就用hash命名
             */
            if (StringUtils.isEmpty(filename)) {
                filename = fileId + "." + fileType;
            }
        }

        /**
         *  上传命名的文件名
         */
        String uploadNamedType;
        if (filename.contains("?")) {
            String prefix = StringUtils.split(filename, "?")[0];
            uploadNamedType = prefix.substring(prefix.lastIndexOf(".") + 1);
        } else {
            uploadNamedType = filename.substring(filename.lastIndexOf("."));
        }

        /**
         *  TODO 如果上传的文件类型，和当前命名的文件类型不一致
         */
        if (!type.equalsIgnoreCase(uploadNamedType)) {
            log.warn("保存文件类型与上传的源文件类型不一致, 上传类型:{},命名类型:{}", type, uploadNamedType);
        }

        UploadFile upload = new UploadFile(req);
        upload.setKey(fileId);
        upload.setType(type);
        upload.setSize(file.getSize());


        UploadEvent event = new UploadEvent(upload, req);

//        /**
//         *  直接执行
//         */
//        if (uploadFilterChain == null) {
//            return UploadResult.result(upload(fileId, file, filename, directory));
//        }

        String finalFilename = filename;
        uploadFilterChain.doFilter(event);
        if (event.getCompleted()) {
            //TODO 如果终结了
        }
        return UploadResult.result(upload(fileId, file, finalFilename, directory));

//        uploadFilterChain.before(event);
//        if (event.getCompleted()) {
//            return UploadResult.result(event.getUpload(UploadFile.class).getUrl());
//        }
//
//
//        String url = null;
//        try {
//            url = upload(fileId, file, filename, directory);
//            event.setCompleted(true);
//            uploadFilterChain.after(event, url);
//            return UploadResult.result(url);
//        } catch (Exception e) {
//            if (!StringUtils.isEmpty(url)) {
//                event.completed(true);
//            }
//            uploadFilterChain.error(event, e);
//
//            url = event.getUpload(UploadFile.class).getUrl();
//
//            if (!StringUtils.isEmpty(url)) {
//                return UploadResult.result(url);
//            }
//            throw e;
//        }
    }


    /**
     * 得到类型
     *
     * @param file 文件
     *
     * @return {@link String}
     *
     * @throws IOException ioexception
     */
    protected String getType(MultipartFile file) throws IOException {
        if (file != null && file.isEmpty()) {
            String type = FileMedia.getType(file.getInputStream());
            if (StringUtils.isEmpty(type)) {
                type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            }
            return type;
        }
        return null;
    }

    /**
     * 验证类型
     *
     * @param file  文件
     * @param types 类型
     *
     * @throws IOException ioexception
     */
    private void verifyType(MultipartFile file, String... types) throws IOException {
        if (file != null && !file.isEmpty()) {
            verifyType(file, getType(file), types);
        } else {
            throw new FileNotFoundException("上传文件为空");
        }
    }

    /**
     * 验证类型
     *
     * @param file  文件
     * @param type  类型
     * @param types 类型
     *
     * @throws IOException ioexception
     */
    private void verifyType(MultipartFile file, String type, String... types) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (types == null && defaultTypes != null) {
                types = defaultTypes;
            }
            if (types != null) {
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

}
