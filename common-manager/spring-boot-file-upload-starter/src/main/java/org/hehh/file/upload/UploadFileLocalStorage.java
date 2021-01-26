package org.hehh.file.upload;

import lombok.extern.slf4j.Slf4j;
import org.hehh.utils.file.FileUtil;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2020-08-22 15:45
 * @description: 上传文件本地存储
 */
@Slf4j
public class UploadFileLocalStorage extends AbstractUploadFileStorage implements UploadFileStorage {


    /**
     * 访问域名
     */
    private final String accessDomain;

    /**
     * 文件存储目录
     */
    private final String directory;


    /**
     * 上传文件的本地存储
     *
     * @param accessDomain 访问域
     * @param directory    目录
     */
    public UploadFileLocalStorage(String accessDomain, String directory) {
        assert accessDomain != null : "文件访问域名不能为空";
        assert directory != null : "文件访问域名对于访问目录不能为空";

        this.accessDomain = accessDomain;
        this.directory = directory;
    }

    /**
     * 上传文件的本地存储
     *
     * @param accessDomain 访问域
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    public UploadFileLocalStorage(String accessDomain) throws FileNotFoundException {
        this(accessDomain, ResourceUtils.getURL("classpath:upload").getPath());
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
    @Override
    protected String upload(String fileId, MultipartFile file, String filename, String directory) throws IOException {
        check(file, directory);
        String fileDirectory = getDirectory(directory);


        try {
            if (StringUtils.isEmpty(filename)) {
                if (!StringUtils.isEmpty(fileId)) {
                    filename = fileId;
                } else {
                    filename = FileUtil.md5(file.getInputStream());
                }

            }
            /**
             *  防止文件没有后缀
             */
            if (!filename.contains(".")) {
                filename = filename + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }
            String fileLocalUrl = fileDirectory.endsWith("/") ? fileDirectory + filename : fileDirectory + "/" + filename;

            File fileStorageLocal = ResourceUtils.getFile(fileLocalUrl);


            /***
             * 检测是否存在目录
             */
            if (!fileStorageLocal.getParentFile().exists()) {
                fileStorageLocal.getParentFile().mkdirs();
            }

            /**
             *  输出到文件
             */
            file.transferTo(fileStorageLocal);
            if (log.isDebugEnabled()) {
                log.debug("Upload file storage to local,originalFilename:{},directory:{} ", file.getOriginalFilename(), fileLocalUrl);
            }

            return this.accessDomain + "/" + StringUtils.replace(fileLocalUrl, this.directory, "");


        } catch (IOException e) {
            log.error("Upload file storage to local error:", e);
        }

        return null;
    }


    /**
     * 验证
     *
     * @param file
     * @param directory
     *
     * @throws FileNotFoundException
     */
    private void check(MultipartFile file, String directory) throws FileNotFoundException {
        if (file == null || file.isEmpty()) {
            throw new FileNotFoundException("MultipartFile 不能为空");
        }
    }


    /**
     * 格式化目录
     *
     * @param directory
     *
     * @return
     */
    private String getDirectory(String directory) {
        String newDirectory = this.directory;
        if (StringUtils.hasText(directory)) {
            directory = StringUtils.replace(directory, "//", "/");
            newDirectory = this.directory + (directory.startsWith("/") ? directory : ("/" + directory));
        }
        return newDirectory;
    }
}
