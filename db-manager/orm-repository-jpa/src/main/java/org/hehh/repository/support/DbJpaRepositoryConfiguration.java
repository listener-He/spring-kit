package org.hehh.repository.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author: HeHui
 * @date: 2020-08-05 23:47
 * @description: 自定义 jpa配置
 */
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = DbJpaRepositoryFactoryBean.class)
public class DbJpaRepositoryConfiguration {
}
