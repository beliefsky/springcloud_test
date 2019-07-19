package com.sky.authentication.wechat.api;

import com.sky.authentication.wechat.entity.WechatUserInfo;

public interface IWechatApi {
    WechatUserInfo getUserInfo(String openId);
}
