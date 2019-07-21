package com.sky.authentication.social;

import com.sky.authentication.social.override.SocialProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.social")
public class SocialProperties {

	private String filterProcessesUrl;
	private SocialProperty	wechat;
}
