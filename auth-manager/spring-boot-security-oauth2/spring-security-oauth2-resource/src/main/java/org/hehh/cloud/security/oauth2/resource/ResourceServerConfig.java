package org.hehh.cloud.security.oauth2.resource;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/**
 * @author: HeHui
 * @date: 2020-08-30 00:11
 * @description: 资源服务配置
 */
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {


    /**
     * Override this method to configure the {@link HttpSecurity}. Typically subclasses
     * should not invoke this method by calling super as it may override their
     * configuration. The default configuration is:
     *
     * <pre>
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * </pre>
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .mvcMatcher("/messages/**")
            .authorizeRequests()
            .mvcMatchers("/messages/**").access("hasAuthority('SCOPE_message.read')")
            .and()
            .oauth2ResourceServer()
            .jwt();
    }
}
