package org.hehh.demo.cloud;

import cn.hutool.crypto.symmetric.AES;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.common.bean.result.SuccessResult;
import org.hehh.cloud.spring.decrypt.annotation.Decrypt;
import org.hehh.cloud.spring.mvc.annotation.Param;
import org.hehh.cloud.spring.mvc.annotation.Required;
import org.hehh.cloud.spring.mvc.util.ObjectMapperKit;
import org.springframework.web.bind.annotation.*;

/**
 * @author: HeHui
 * @create: 2020-03-22 01:15
 * @description: aa
 **/
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping
    public Result test(@Required String msg, @RequestParam  Integer code){
        return SuccessResult.succeed(msg);
    }




    @GetMapping("/2")
    public Result test1(@RequestBody @Decrypt Result<String> test1){
        return test1;
    }

    public static void main(String[] args) {
        AES aes = new AES("bdb356ec3d924a98800a68637e705af6".getBytes());
        Result<String> a = SuccessResult.succeed("测试消息","哦豁!");
        String hex = aes.encryptHex(ObjectMapperKit.toJsonStr(a));

        System.out.println(hex);
    }
}
