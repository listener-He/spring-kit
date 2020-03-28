package org.hehh.demo.cloud;

import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.spring.decrypt.annotation.Decrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: HeHui
 * @create: 2020-03-22 01:15
 * @description: aa
 **/
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping
    public Result test(@Decrypt Result<List<String>> result){
        return result;
    }
}
