package com.sky.authentication.wechat.api;

import com.sky.authentication.wechat.entity.WeChatUserInfo;

public interface IWeChatApi {
    WeChatUserInfo getUserInfo(String openId);
}
