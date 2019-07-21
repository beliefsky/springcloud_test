package com.sky.authentication.social.wechat.properties;

import com.sky.authentication.social.SocialProperties;
import com.sky.authentication.social.wechat.factory.WechatConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;

// https://blog.csdn.net/mr_zhuqiang/article/details/81942534
@Configuration
@ConditionalOnProperty(prefix = "spring.social.wechat", name = "app-id")
public class WechatAutoConfiguration extends SocialConfigurerAdapter {

	@Autowired
	private SocialProperties socialProperties;


    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer,
                                       Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    private ConnectionFactory<?> createConnectionFactory() {
        return new WechatConnectionFactory(
        	"wechat",
			socialProperties.getWechat().getAppId(),
			socialProperties.getWechat().getAppSecret()
        );
    }

//    @Bean({"connect/weixinConnect", "connect/weixinConnected"})
//    @ConditionalOnMissingBean(name = "weixinConnectedView")
//    public View weixinConnectedView() {
//        return new MyConnectView();
//    }

    // 后补：做到处理注册逻辑的时候发现的一个bug：登录完成后，数据库没有数据，但是再次登录却不用注册了
    // 就怀疑是否是在内存中存储了。结果果然发现这里父类的内存ConnectionRepository覆盖了SocialConfig中配置的jdbcConnectionRepository
    // 这里需要返回null，否则会返回内存的 ConnectionRepository
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

}
