package org.hehh.cloud.spring.mvc.annotation;

import org.hehh.cloud.spring.core.CoreConfiguration;
import org.hehh.cloud.spring.mvc.config.WebMvcRegistrationsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author : HeHui
 * @date : 2019-04-23 14:16
 * @describe : 启用增强请求
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({CoreConfiguration.class, WebMvcRegistrationsConfiguration.class})
public @interface EnableEnhanceRequest {
}
