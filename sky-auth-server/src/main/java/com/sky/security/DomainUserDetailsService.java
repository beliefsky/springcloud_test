package com.sky.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DomainUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        System.out.println(AuthorityUtils.createAuthorityList("ADMIN"));

        String[] auth = {"QUERY_DEMO", "DELETE_DEMO"};

        return new User(s, "{bcrypt}$2a$10$Yu2fbUjm/ZtA.hhHhiI1su6RK8SaNIn6aQT5sMKLzzuGQM3HTOZjm", AuthorityUtils.createAuthorityList(auth));

//        throw  new UsernameNotFoundException("用户["+username+"]不存在");
    }
}
