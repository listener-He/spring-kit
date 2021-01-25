package org.hehh.utils.file.qiniu;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;

import java.io.IOException;

/**
 * @author: HeHui
 * @date: 2020-08-22 17:23
 * @description: 七牛云构建器
 */
public class QiniuBuild {


    /**
     * 构建
     *
     * @param parameters 参数
     *
     * @return {@link QiNiuFileUploadImpl}* @throws IOException ioexception
     */
    public static QiNiuFileUploadImpl build(QiniuyunConfigurationParameters parameters) throws IOException {
        com.qiniu.storage.Configuration configuration = qiniuConfig(parameters);
        Auth auth = auth(parameters);
        return new QiNiuFileUploadImpl(uploadManager(configuration, parameters), new BucketManager(auth, configuration), auth, parameters.getDefaultBucket());
    }


    /**
     * 构建
     *
     * @param zone 区
     *
     * @return {@link Region}
     */
    public static Region buildRegion(String zone) {
        Region region;
        switch (zone) {
            case "z0":
                region = Region.region0();
                break;
            case "z1":
                region = Region.region1();
                break;
            case "z2":
                region = Region.region2();
                break;
            case "na0":
                region = Region.regionNa0();
                break;
            case "as0":
                region = Region.regionAs0();
                break;
            default:
                // Default is detecting zone automatically
                region = Region.autoRegion();
                break;
        }
        return region;
    }

    /**
     * 机房,配置自己空间所在的区域
     */
    public static com.qiniu.storage.Configuration qiniuConfig(QiniuyunConfigurationParameters parameters) {
        return new com.qiniu.storage.Configuration(QiniuBuild.buildRegion(parameters.getZone()));
    }

    /**
     * 构建一个七牛上传工具实例
     */
    public static UploadManager uploadManager(com.qiniu.storage.Configuration configuration, QiniuyunConfigurationParameters parameters) throws IOException {
        return new UploadManager(configuration, new FileRecorder(parameters.getBlockDirectory()));
    }


    /**
     * 认证信息实例
     *
     * @return
     */
    public static Auth auth(QiniuyunConfigurationParameters parameters) {
        return Auth.create(parameters.getAccessKey(), parameters.getSecretKey());
    }
}
