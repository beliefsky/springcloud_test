package com.sky.authentication.wechat.adapter;

import com.sky.authentication.wechat.api.IWechatApi;
import com.sky.authentication.wechat.entity.WechatUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

public class WechatAdapter implements ApiAdapter<IWechatApi> {
    private String openId;

    public WechatAdapter() {
    }

    public WechatAdapter(String openId) {
        this.openId = openId;
    }


    @Override
    public boolean test(IWechatApi api) {
        return true;
    }

    @Override
    public void setConnectionValues(IWechatApi api, ConnectionValues values) {
        WechatUserInfo userInfo = api.getUserInfo(openId);
        values.setProviderUserId(userInfo.getOpenid());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(IWechatApi api) {
        return null;
    }

    @Override
    public void updateStatus(IWechatApi api, String message) {

    }
}
