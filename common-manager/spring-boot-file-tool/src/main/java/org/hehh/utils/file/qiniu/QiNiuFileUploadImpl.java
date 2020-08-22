package org.hehh.utils.file.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author: HeHui
 * @date: 2020-08-22 17:17
 * @description: 七牛云文件上传实现类
 */
@Slf4j
public class QiNiuFileUploadImpl implements QiNiuFileUpload {

    /**
     * 上传管理
     */
    private final UploadManager uploadManager;

    /**
     * 桶经理
     */
    private final BucketManager bucketManager;




    /**
     * 身份验证
     */
    private final Auth auth;


    private final StringMap putPolicy;


    private final Bucket defaultBucket;

    /**
     * 气妞妞文件上传impl
     * @param uploadManager 上传管理
     * @param bucketManager 桶经理
     * @param auth 授权
     * @param defaultBucket 默认空间
     */
    public QiNiuFileUploadImpl(UploadManager uploadManager, BucketManager bucketManager,  Auth auth, Bucket defaultBucket) {
        this.uploadManager = uploadManager;
        this.bucketManager = bucketManager;
        this.auth = auth;
        this.defaultBucket = defaultBucket;

        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\"}");
    }


    /**
     * 上传
     *
     * @param inputStream 输入流
     * @param filename    文件名
     * @param directory   目录
     * @param bucket      桶
     * @return {@link String}
     * @throws FileNotFoundException 文件未发现异常
     */
    @Override
    public String upload(InputStream inputStream, String filename, String directory, Bucket bucket) throws FileNotFoundException {
        try {
            String key = StringUtils.replace(directory + "/" + filename, "//", "/");

            Response response = this.uploadManager.put(inputStream, filename, uploadToken(key, 3600, bucket), this.putPolicy, null);
            if(response.isOK()){
                return bucket.getDomain() + "/" + response.jsonToMap().get("key").toString();
            }else{
                throw new FileNotFoundException("七牛云上传文件失败:"+response.error);
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 上传
     *
     * @param file     文件
     * @param filename 文件名
     * @param bucket   桶
     * @return {@link String}
     * @throws QiniuException qiniu例外
     */
    @Override
    public String upload(File file, String filename, Bucket bucket) throws FileNotFoundException {
        try {
            Response response = this.uploadManager.put(file, filename, uploadToken(filename, 3600, bucket));
            if(response.isOK()){
                return bucket.getDomain() + "/" + response.jsonToMap().get("key").toString();
            }else{
                throw new FileNotFoundException("七牛云上传文件失败:"+response.error);
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传
     *
     * @param url      url
     * @param filename 文件名
     * @param bucket   桶
     * @return {@link String}
     */
    @Override
    public String upload(String url, String filename, Bucket bucket) throws QiniuException {
        FetchRet fetchRet = bucketManager.fetch(url, bucket.getName(), filename);
        return bucket.getDomain() + "/" +fetchRet.key;
    }



    /**
     * 删除
     *
     * @param url    url
     * @param bucket 桶
     * @return {@link Response}
     */
    @Override
    public Response delete(String url, Bucket bucket) throws QiniuException {
        if(bucket == null){
            bucket = defaultBucket;
        }
        String key = StringUtils.replace(url, bucket.getDomain(), "");
        return bucketManager.delete(bucket.getName(),key);
    }

    /**
     * 删除后
     *
     * @param url    url
     * @param days   天
     * @param bucket 桶
     * @return {@link Response}
     */
    @Override
    public Response deleteAfter(String url, int days, Bucket bucket) throws QiniuException {
        if(bucket == null){
            bucket = defaultBucket;
        }
        String key = StringUtils.replace(url, bucket.getDomain(), "");

        return bucketManager.deleteAfterDays(bucket.getName(),key,days);
    }

    /**
     * 上传的令牌
     *
     * @param filename 文件名
     * @param expires  到期
     * @param bucket   桶
     * @return {@link String}
     */
    @Override
    public String uploadToken(String filename, long expires, Bucket bucket) {
        if(bucket == null){
            bucket = defaultBucket;
        }
        return this.auth.uploadToken(bucket.getName(), filename, expires, putPolicy);
    }



    /**
     * 生成私人网址
     *
     * @param url             url
     * @param expireInSeconds 在几秒钟内到期
     * @param bucket          桶
     * @return {@link String}
     */
    @Override
    public String generatePrivateUrl(String url, long expireInSeconds, Bucket bucket) {
        String encodedFileName = null;
        if(bucket == null){
            bucket = defaultBucket;
        }
        String key = StringUtils.replace(url, bucket.getDomain(), "");
        try {
            encodedFileName = URLEncoder.encode(key, "utf-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String publicUrl = String.format("%s/%s", bucket.getDomain(), encodedFileName);
        return auth.privateDownloadUrl(publicUrl, expireInSeconds);
    }


}
