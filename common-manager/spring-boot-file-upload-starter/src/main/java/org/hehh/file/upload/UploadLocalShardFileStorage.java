package org.hehh.file.upload;

import lombok.extern.slf4j.Slf4j;
import org.hehh.file.upload.event.UploadFilterChain;
import org.hehh.file.upload.req.UploadShardFile;
import org.hehh.utils.file.FileUtil;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2021-02-19 11:19
 * @description: 上传本地分片文件
 */
@Slf4j
public class UploadLocalShardFileStorage extends AbstractUploadShardFileStorage {


    /**
     * 文件存储目录
     */
    private final String directory;


    /**
     * 摘要上传碎片文件存储
     *
     * @param directory
     * @param defaultTypes 默认类型
     */
    public UploadLocalShardFileStorage(String directory, String... defaultTypes) {
        this(null, directory, defaultTypes);
    }

    /**
     * 摘要上传碎片文件存储
     *
     * @param uploadFilterChain 上传过滤器链
     * @param directory
     * @param defaultTypes      默认类型
     */
    public UploadLocalShardFileStorage(UploadFilterChain uploadFilterChain, String directory, String... defaultTypes) {
        super(uploadFilterChain, defaultTypes);
        assert ResourceUtils.isUrl(directory) : "目录格式非法";
        this.directory = directory;
    }

    /**
     * 摘要上传碎片文件存储
     *
     * @param uploadFilterChain 上传过滤器链
     * @param directory
     */
    public UploadLocalShardFileStorage(UploadFilterChain uploadFilterChain, String directory) {
        this(uploadFilterChain, directory, null);
    }

    /**
     * 摘要上传碎片文件存储
     *
     * @param directory
     */
    public UploadLocalShardFileStorage(String directory) {
        this(null, directory);
    }

    /**
     * 上传本地碎片文件存储
     *
     * @throws FileNotFoundException 文件未发现异常
     */
    public UploadLocalShardFileStorage() throws FileNotFoundException {
        this(ResourceUtils.getURL("classpath:/upload/chunk").getPath());
    }


    /**
     * 获取不重复文件名
     *
     * @param fileDirectory 文件目录
     * @param fileName      文件名称
     * @param index         指数
     *
     * @return {@link File}
     */
    private File getNoRepeat(File fileDirectory, String fileName, int index) {
        File file = new File(fileDirectory, fileName + "-" + index);
        if (file.exists()) {
            return getNoRepeat(fileDirectory, fileName, index++);
        }
        return file;
    }


    /**
     * 上传
     *
     * @param shardFile 碎片文件
     */
    @Override
    public void upload(UploadShardFile shardFile) {


        File fileDirectory = new File(directory, shardFile.getIdentifier());
        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs();
        }

        /**
         *  记录上传最后更新时间
         */
        FileUtil.tempLastModified(fileDirectory);

        try {
            File file = new File(fileDirectory, String.valueOf(shardFile.getChunkNumber()));
            if (file.exists()) {
                file = getNoRepeat(fileDirectory,String.valueOf(shardFile.getChunkNumber()),1);
                log.error("碎片文件{}已存在,本次上传命名为{},文件hash:{}",shardFile.getChunkNumber(),file.getName(),shardFile.getIdentifier());
            }

            shardFile.getFile().transferTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
