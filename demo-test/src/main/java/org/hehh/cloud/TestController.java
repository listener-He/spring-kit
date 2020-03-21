package org.hehh.cloud;

import org.hehh.cloud.common.bean.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: HeHui
 * @create: 2020-03-22 01:15
 * @description: aa
 **/
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping
    public Result test(@RequestBody Result result){
        return result;
    }
}
