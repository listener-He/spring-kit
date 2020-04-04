package org.hehh.demo;

import org.hehh.cloud.spring.mvc.config.EnableEnhanceRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: HeHui
 * @create: 2020-03-22 00:56
 * @description: demo程序启动
 **/
@SpringBootApplication
@EnableEnhanceRequest
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }
}
