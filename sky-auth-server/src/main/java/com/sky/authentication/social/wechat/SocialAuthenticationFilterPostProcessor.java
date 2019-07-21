package com.sky.authentication.social.wechat;

import org.springframework.social.security.SocialAuthenticationFilter;


public interface SocialAuthenticationFilterPostProcessor {

    void process(SocialAuthenticationFilter socialAuthenticationFilter);
}
