package org.hehh.file.upload;

import lombok.extern.slf4j.Slf4j;
import org.hehh.file.upload.event.UploadMultipartFileEvent;
import org.hehh.file.upload.event.UploadMultipartFileEventPublisher;
import org.hehh.file.upload.exception.UploadFileException;
import org.hehh.utils.file.FileUtil;
import org.hehh.utils.file.pojo.FileMedia;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.*;
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
public abstract class AbstractUploadFileStorage implements UploadFileStorage, ApplicationContextAware {

    private final String[] defaultTypes;

    private UploadMultipartFileEventPublisher eventPublisher;

    /**
     * 抽象上传文件存储
     *
     * @param defaultTypes 默认类型
     */
    public AbstractUploadFileStorage(String... defaultTypes) {
        this.defaultTypes = defaultTypes;
    }

    /**
     * 抽象上传文件存储
     *
     * @param eventPublisher 事件发布者
     * @param defaultTypes   默认类型
     */
    public AbstractUploadFileStorage(UploadMultipartFileEventPublisher eventPublisher, String... defaultTypes) {
        this.defaultTypes = defaultTypes;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 抽象上传文件存储
     *
     * @param eventPublisher 事件发布者
     */
    public AbstractUploadFileStorage(UploadMultipartFileEventPublisher eventPublisher) {
        this();
        this.eventPublisher = eventPublisher;
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
     * @return {@link String}
     */
    @Override
    public String upload(UploadMultipartFileReq req, String directory, String... types) throws IOException {
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

        /**
         *  直接执行
         */
        if (eventPublisher == null) {
            return upload(fileId, file, filename, directory);
        }

        UploadMultipartFileEvent uploadEvent = new UploadMultipartFileEvent();
        uploadEvent.settingUploadUser(req);
        uploadEvent.setKey(fileId);
        uploadEvent.setType(type);
        uploadEvent.setSize(file.getSize());

        eventPublisher.before(uploadEvent);

        try {
            String url = upload(fileId, file, filename, directory);
            eventPublisher.after(uploadEvent, url);
            return url;
        } catch (Exception e) {
            eventPublisher.error(uploadEvent, e);
            throw e;
        }
    }


    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     *
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (eventPublisher == null) {
            applicationContext.getBean(UploadMultipartFileEventPublisher.class);
        }
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
