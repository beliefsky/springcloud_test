package com.sky.authentication.social.wechat.api;

import com.sky.authentication.social.wechat.entity.WechatUserInfo;

public interface IWechatApi {
    WechatUserInfo getUserInfo(String openId);
}
