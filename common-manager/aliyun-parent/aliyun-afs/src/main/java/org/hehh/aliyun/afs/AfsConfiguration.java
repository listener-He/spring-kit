package org.hehh.aliyun.afs;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.hehh.aliyun.afs.impl.AiMachineAuth;
import org.hehh.aliyun.afs.impl.AliyunTracelessMachineAuth;
import org.hehh.aliyun.afs.impl.SlideMachineAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author: HeHui
 * @date: 2020-03-25 16:55
 * @description: 人机验证配置
 */
@Configuration
@EnableConfigurationProperties(AliyunAfsParameter.class)
@ConditionalOnProperty(value = "aliyun.afs.enable",havingValue = "true")
public class AfsConfiguration {

    @Autowired
    private AliyunAfsParameter aliyunAfsParameter;


    /**
     *  人机验证请求类
     * @return
     * @throws ClientException
     */
    @Bean
    @Primary
    public IAcsClient acsClient() throws ClientException {
        IClientProfile profile= DefaultProfile.getProfile(aliyunAfsParameter.getRegionId(), aliyunAfsParameter.getAccessKey(), aliyunAfsParameter.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        DefaultProfile.addEndpoint(aliyunAfsParameter.getRegionId(), aliyunAfsParameter.getRegionId(), "afs", "afs.aliyuncs.com");
        return client;
    }


    /**
     *  无痕验证
     * @param client
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "aliyun.afs.model",havingValue = "traceless")
    public ITracelessMachineAuth tracelessMachineAuth(IAcsClient client){
        return new AliyunTracelessMachineAuth(client);
    }


    /**
     *  智能验证
     * @param client
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "aliyun.afs.model",havingValue = "ai")
    public IMachineAuth aiMachineAuth(IAcsClient client){
        return new AiMachineAuth(client,aliyunAfsParameter.getAppKey());
    }



    /**
     *  滑动验证
     * @param client
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnProperty(value = "aliyun.afs.model",havingValue = "slide",matchIfMissing = true)
    public IMachineAuth slideMachineAuth(IAcsClient client){
        return new SlideMachineAuth(client,aliyunAfsParameter.getAppKey());
    }
}


