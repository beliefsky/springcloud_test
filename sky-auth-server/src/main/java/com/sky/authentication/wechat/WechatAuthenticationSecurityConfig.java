package com.sky.authentication.wechat;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class WechatAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        WechatAuthenticationFilter filter = new WechatAuthenticationFilter();
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
//        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);

        WechatAuthenticationProvider provider = new WechatAuthenticationProvider();
//        provider.setUserDetailsService(new DomainUserDetailsService());

        http
                // 注册到AuthenticationManager中去
                .authenticationProvider(provider)
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
