package org.hehh.cloud.spring.decrypt.annotation;

import java.lang.annotation.*;

/**
 * @author: HeHui
 * @create: 2020-03-21 16:58
 * @description: 解密字段
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptField {
}
