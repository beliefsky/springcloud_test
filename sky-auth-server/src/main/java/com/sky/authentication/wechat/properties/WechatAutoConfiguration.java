package com.sky.authentication.wechat.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;

// https://blog.csdn.net/mr_zhuqiang/article/details/81942534
@Configuration
@ConditionalOnProperty(prefix = "system.social.weixin", name = "app-id")
public class WechatAutoConfiguration extends SocialConfigurerAdapter {
}
