package com.sky.authentication.social.override;

import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

public abstract class SocialAutoConfigurerAdapter extends SocialConfigurerAdapter {

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
		connectionFactoryConfigurer.addConnectionFactory(this.createConnectionFactory());
	}

	protected abstract ConnectionFactory<?> createConnectionFactory();
}
