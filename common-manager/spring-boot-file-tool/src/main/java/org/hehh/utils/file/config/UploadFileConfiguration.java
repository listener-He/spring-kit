package org.hehh.utils.file.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.hehh.utils.file.UploadFileStorage;
import org.hehh.utils.file.impl.UploadFileLocalStorage;
import org.hehh.utils.file.qiniu.QiNiuFileUploadImpl;
import org.hehh.utils.file.qiniu.QiniuyunConfigurationParameters;
import org.hehh.utils.file.qiniu.RegionBuild;
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
    public LocalUploadParameters localUploadParameters(){
        return new LocalUploadParameters();
    }


    /**
     * 本地上传文件存储
     *
     * @param parameters 参数
     * @return {@link UploadFileStorage}
     */
    @Bean
    @Primary
    public UploadFileStorage localUploadFileStorage(LocalUploadParameters parameters){
        return new UploadFileLocalStorage(parameters.getDomain(),parameters.getDirectory());
    }



    /**
     * 气妞妞上传文件配置
     *
     * @author hehui
     * @date 2020/08/22
     */
    @Configuration
    @ConditionalOnClass(UploadManager.class)
    static class QiNiuUploadFileConfiguration{

        /**
         * 七牛云配置参数
         *
         * @return {@link QiniuyunConfigurationParameters}
         */
        @Bean
        @ConfigurationProperties("upload.file.qiniu")
        public QiniuyunConfigurationParameters qiniuyunConfigurationParameters(){
            return new QiniuyunConfigurationParameters();
        }


        /**
         * 七牛云文件上传
         *
         * @param parameters 参数
         * @return {@link UploadFileStorage}
         * @throws IOException ioexception
         */
        @Bean
        public UploadFileStorage qiNiuFileUploadImpl(QiniuyunConfigurationParameters parameters) throws IOException {
            com.qiniu.storage.Configuration configuration = qiniuConfig(parameters);
            Auth auth = auth(parameters);
            return new QiNiuFileUploadImpl(uploadManager(configuration,parameters),new BucketManager(auth,configuration),auth,parameters.getDefaultBucket());
        }


        /**
         * 机房,配置自己空间所在的区域
         */
        public com.qiniu.storage.Configuration qiniuConfig(QiniuyunConfigurationParameters parameters) {
            return new com.qiniu.storage.Configuration(RegionBuild.build(parameters.getZone()));
        }

        /**
         * 构建一个七牛上传工具实例
         */
        public UploadManager uploadManager(com.qiniu.storage.Configuration configuration,QiniuyunConfigurationParameters parameters) throws IOException {
            return new UploadManager(configuration,new FileRecorder(parameters.getBlockDirectory()));
        }



        /**
         * 认证信息实例
         * @return
         */
        public Auth auth(QiniuyunConfigurationParameters parameters) {
            return Auth.create(parameters.getAccessKey(), parameters.getSecretKey());
        }
    }
}
