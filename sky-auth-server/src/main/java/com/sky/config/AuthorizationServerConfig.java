package com.sky.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;
    //存储链接
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    //用户信息相关的实现
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;


    //token存放位置
    @Bean
    public RedisTokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }


    //配置认证管理器以及用户信息业务实现
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)//若无，refresh_token会有UserDetailsService is required错误
                .tokenStore(tokenStore());
    }


    //配置认证规则，那些需要认证那些不需要
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }


    //配置客户端
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        PigClientDetailsService clientDetailsService = new PigClientDetailsService(dataSource);
//        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
//        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
//        clients.withClientDetails(clientDetailsService);

        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }
}
