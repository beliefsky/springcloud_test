package com.sky.authentication.wechat.factory;

import com.sky.authentication.wechat.WeChatAccessGrant;
import com.sky.authentication.wechat.adapter.WeChatAdapter;
import com.sky.authentication.wechat.api.IWeChatApi;
import com.sky.authentication.wechat.provider.WeChatServiceProvider;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

public class WeChatConnectionFactory extends OAuth2ConnectionFactory<IWeChatApi> {

    /**
     * @param appId
     * @param appSecret
     */
    public WeChatConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeChatServiceProvider(appId, appSecret), new WeChatAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeChatAccessGrant) {
            return ((WeChatAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    @Override
    public Connection<IWeChatApi> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    @Override
    public Connection<IWeChatApi> createConnection(ConnectionData data) {
        return new OAuth2Connection<>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<IWeChatApi> getApiAdapter(String providerUserId) {
        return new WeChatAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<IWeChatApi> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<IWeChatApi>) getServiceProvider();
    }
}
