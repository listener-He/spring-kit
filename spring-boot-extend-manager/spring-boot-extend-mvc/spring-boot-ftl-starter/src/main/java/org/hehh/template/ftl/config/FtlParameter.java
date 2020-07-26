package org.hehh.template.ftl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Map;

/**
 * @author: HeHui
 * @create: 2019-11-20 11:51
 * @description: ftl配置
 **/
@ConfigurationProperties(prefix = "spring.ftl")
@EnableConfigurationProperties
@Data
public class FtlParameter {


    /**
     *  远程模版地址
     */
    private String remotePath;


    /**
     *  本地模版地址
     */
    private String packagePath;



    /**
     *  全局参数
     */
    private Map<String,String> globalParam;

}
