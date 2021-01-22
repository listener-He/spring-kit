package org.hehh.utils.file.config;

import com.qiniu.storage.UploadManager;
import org.hehh.utils.file.UploadFileStorage;
import org.hehh.utils.file.impl.UploadFileLocalStorage;
import org.hehh.utils.file.qiniu.QiniuBuild;
import org.hehh.utils.file.qiniu.QiniuyunConfigurationParameters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2020-08-22 18:01
 * @description: 上传文件配置
 */
@Configuration
public class UploadFileConfiguration {


    /**
     * 本地上传的参数
     *
     * @return {@link LocalUploadParameters}
     */
    @Bean
    @ConfigurationProperties("upload.file.local")
    public LocalUploadParameters localUploadParameters() {
        return new LocalUploadParameters();
    }


    /**
     * 本地上传文件存储
     *
     * @param parameters 参数
     *
     * @return {@link UploadFileStorage}
     */
    @Bean
    @Primary
    public UploadFileStorage localUploadFileStorage(LocalUploadParameters parameters) {
        return new UploadFileLocalStorage(parameters.getDomain(), parameters.getDirectory());
    }


    /**
     * 气妞妞上传文件配置
     *
     * @author hehui
     * @date 2020/08/22
     */
    @Configuration
    @ConditionalOnClass(UploadManager.class)
    static class QiNiuUploadFileConfiguration {

        /**
         * 七牛云配置参数
         *
         * @return {@link QiniuyunConfigurationParameters}
         */
        @Bean
        @ConfigurationProperties("upload.file.qiniu")
        public QiniuyunConfigurationParameters qiniuyunConfigurationParameters() {
            return new QiniuyunConfigurationParameters();
        }


        /**
         * 七牛云文件上传
         *
         * @param parameters 参数
         *
         * @return {@link UploadFileStorage}
         *
         * @throws IOException ioexception
         */
        @Bean
        public UploadFileStorage qiNiuFileUploadImpl(QiniuyunConfigurationParameters parameters) throws IOException {
            return QiniuBuild.build(parameters);
        }

    }
}
