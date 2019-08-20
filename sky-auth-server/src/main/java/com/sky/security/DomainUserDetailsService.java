package com.sky.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

@Slf4j
public class DomainUserDetailsService implements UserDetailsService, SocialUserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        s = "123";
       log.info("select username: " + s);

        String[] auth = {"QUERY_DEMO", "DELETE_DEMO"};

        return new User(s, "{noop}123456", AuthorityUtils.createAuthorityList(auth));

//        throw  new UsernameNotFoundException("用户["+username+"]不存在");
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        log.info("select userId: " + userId);

        return new SocialUser(userId ,"{bcrypt}$2a$10$Yu2fbUjm/ZtA.hhHhiI1su6RK8SaNIn6aQT5sMKLzzuGQM3HTOZjm",true,true,true,true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
    }
}
