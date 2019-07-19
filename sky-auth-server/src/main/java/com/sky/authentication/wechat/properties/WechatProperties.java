package com.sky.authentication.wechat.properties;

import lombok.Data;

@Data
public class WechatProperties {

    /**
     * 第三方id，用来决定发起第三方登录的url，默认是 weixin。
     */
    private String providerId = "weixin";

    private String appId;//应用id

    private String appSecret;//应用密匙
}
