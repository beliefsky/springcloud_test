package com.sky.authentication.wechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class WechatAuthenticationProvider implements AuthenticationProvider {

    private final String WECHAT_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private final String WECHAT_APP_ID = "wx4a11ae335e843db1";
    private final String WECHAT_APP_SECRET = "09069ff8aa678e0262b397b65dd0d43e";

    private final ObjectMapper objectMapper;


    public WechatAuthenticationProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken token = (WechatAuthenticationToken) authentication;

        String sb = WECHAT_ACCESS_TOKEN +
                "?appid=" +
                WECHAT_APP_ID +
                "&secret=" +
                WECHAT_APP_SECRET +
                "&code=" +
                token.getPrincipal() +
                "&grant_type=authorization_code";

        String res = HttpConnectionPoolUtil.get(sb);
        if (null == res) {

        }
        try {
            WechatAccessToken accessToken = objectMapper.readValue(res, WechatAccessToken.class);
            if (null != accessToken.getUnionid()) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(WechatAuthenticationToken.class);
    }
}
