package com.sky.authentication.phone;

import com.sky.security.DomainUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class PhoneAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    //    @Autowired
//    private AuthenticationFailureHandler authenticationFailureHandler;
//    @Autowired
//    private AuthenticationSuccessHandler authenticationSuccessHandler;
    //实现类怎么确定？ 自定义的实现？？
//    @Autowired
//    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) {
        PhoneAuthenticationFilter filter = new PhoneAuthenticationFilter();
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
//        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);

        PhoneAuthenticationProvider provider = new PhoneAuthenticationProvider();
        provider.setUserDetailsService(new DomainUserDetailsService());

        http
                // 注册到AuthenticationManager中去
                .authenticationProvider(provider)
                // 添加到 UsernamePasswordAuthenticationFilter 之后
                // 貌似所有的入口都是 UsernamePasswordAuthenticationFilter
                // 然后UsernamePasswordAuthenticationFilter的provider不支持这个地址的请求
                // 所以就会落在我们自己的认证过滤器上。完成接下来的认证
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
