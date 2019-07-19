package com.sky.authentication.wechat.entity;

import lombok.Data;

@Data
public class WeChatResult {
    private String errcode;
    private String errmsg;

    private String access_token;
    private String scope;
    private String refresh_token;
    private Long expires_in;
    private String openid;
}
