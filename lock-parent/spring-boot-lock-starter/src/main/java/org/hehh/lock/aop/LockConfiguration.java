package org.hehh.lock.aop;

import org.hehh.lock.ILock;
import org.hehh.lock.LocalReadLock;
import org.hehh.lock.RedisLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;

/**
 * @author: HeHui
 * @date: 2020-07-28 00:41
 * @description: lock配置类
 */
@Configuration
public class LockConfiguration {


    /**
     * 本地读锁
     *
     * @return {@link ILock}
     */
    @Bean
    @ConditionalOnMissingBean(name = "localReadLock")
    public ILock localReadLock() {
        return new LocalReadLock();
    }


    /**
     * redis lock 配置
     */
    @Configuration
    @ConditionalOnProperty(prefix = "spring.lock.model", havingValue = "redis")
    @ConditionalOnClass(RedisConnectionFactory.class)
    static class RedisLockConfiguration {

        @Value("${spring.application.name:null}")
        private String serviceName;


        @Bean
        @Primary
        public ILock redisLock(RedisOperations<String, String> redisOperations) {
            return new RedisLock(redisOperations, serviceName);
        }


    }


    /**
     * aop配置
     */
    @Configuration
    @ConditionalOnBean(ILock.class)
    static class LockAopConfiguration {


        @Bean
        public LockAop lockAop(ILock defaultLock, ApplicationContext applicationContext) {
            return new LockAop(defaultLock, applicationContext);
        }

    }
}
