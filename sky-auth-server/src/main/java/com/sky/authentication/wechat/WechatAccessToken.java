package com.sky.authentication.wechat;

import lombok.Data;

@Data
public class WechatAccessToken {
    private Integer errcode;
    private String errmsg;

    private String access_token;
    private String scope;
    private String refresh_token;
    private Long expires_in;
    private String openid;
    private String unionid;
}
