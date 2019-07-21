package com.sky.authentication.social.wechat.factory;

import com.sky.authentication.social.wechat.WechatAccessGrant;
import com.sky.authentication.social.wechat.adapter.WechatAdapter;
import com.sky.authentication.social.wechat.api.IWechatApi;
import com.sky.authentication.social.wechat.provider.WechatServiceProvider;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

public class WechatConnectionFactory extends OAuth2ConnectionFactory<IWechatApi> {

    /**
     * @param appId
     * @param appSecret
     */
    public WechatConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WechatServiceProvider(appId, appSecret), new WechatAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WechatAccessGrant) {
            return ((WechatAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    @Override
    public Connection<IWechatApi> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    @Override
    public Connection<IWechatApi> createConnection(ConnectionData data) {
        return new OAuth2Connection<>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<IWechatApi> getApiAdapter(String providerUserId) {
        return new WechatAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<IWechatApi> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<IWechatApi>) getServiceProvider();
    }
}
