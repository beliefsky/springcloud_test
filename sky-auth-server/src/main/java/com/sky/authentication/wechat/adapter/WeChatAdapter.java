package com.sky.authentication.wechat.adapter;

import com.sky.authentication.wechat.api.IWeChatApi;
import com.sky.authentication.wechat.entity.WeChatUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

public class WeChatAdapter implements ApiAdapter<IWeChatApi> {
    private String openId;

    public WeChatAdapter() {
    }

    public WeChatAdapter(String openId) {
        this.openId = openId;
    }


    @Override
    public boolean test(IWeChatApi api) {
        return true;
    }

    @Override
    public void setConnectionValues(IWeChatApi api, ConnectionValues values) {
        WeChatUserInfo userInfo = api.getUserInfo(openId);
        values.setProviderUserId(userInfo.getOpenid());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(IWeChatApi api) {
        return null;
    }

    @Override
    public void updateStatus(IWeChatApi api, String message) {

    }
}
