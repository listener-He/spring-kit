package org.hehh.cloud.security.oauth2.server.authorization;

import org.hehh.cloud.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.hehh.cloud.security.oauth2.server.authorization.client.RegisteredClientLoading;
import org.hehh.cloud.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keys.KeyManager;
import org.springframework.security.crypto.keys.StaticKeyGeneratingKeyManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author: HeHui
 * @date: 2020-08-29 23:07
 * @description: 认证服务配置
 *
 */
@EnableWebSecurity
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig  {


    /**
     * 注册客户端库
     *
     * @param registeredClientLoading 注册客户端加载
     * @return {@link RegisteredClientRepository}
     */
    @Bean
    @ConditionalOnMissingBean(org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository.class)
    @ConditionalOnBean(RegisteredClientLoading.class)
    public RegisteredClientRepository registeredClientRepository(RegisteredClientLoading registeredClientLoading){
        return new InMemoryRegisteredClientRepository(registeredClientLoading.loading());
    }


    /**
     * 键管理器
     *
     * @return {@link KeyManager}
     */
    @Bean
    @ConditionalOnMissingBean(KeyManager.class)
    public KeyManager keyManager() {
        return new StaticKeyGeneratingKeyManager();
    }



    /**
     * 密码编码器
     *
     * @return {@link PasswordEncoder}
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //TODO 待实现 org.springframework.security.core.userdetails.UserDetailsService
}
