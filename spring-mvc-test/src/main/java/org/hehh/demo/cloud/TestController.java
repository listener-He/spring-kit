package org.hehh.demo.cloud;

import cn.hutool.crypto.symmetric.AES;
import org.hehh.cloud.common.bean.result.Result;
import org.hehh.cloud.common.bean.result.SuccessResult;
import org.hehh.cloud.spring.decrypt.annotation.Decrypt;
import org.hehh.cloud.spring.mvc.annotation.Param;
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
    public Result test(@Decrypt String msg,@RequestParam @Decrypt Integer code){
        return SuccessResult.succeed(msg);
    }





    @GetMapping("/2")
    public Result test1(@Param Result<String> test1){
        return test1;
    }

    public static void main(String[] args) {
        AES aes = new AES("bdb356ec3d924a98800a68637e705af6".getBytes());
        String s = aes.encryptHex("0");
        String s1 = aes.encryptHex("测试消息");
        String s2 = aes.encryptHex("测试数据");

        String str = ObjectMapperKit.toJsonStr(SuccessResult.succeed("测试"));
        String s3 = aes.encryptHex(str);

        System.out.println(str);
    }
}
