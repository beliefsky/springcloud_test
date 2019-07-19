package com.sky.authentication.wechat.provider;

import com.sky.authentication.wechat.api.IWeChatApi;
import com.sky.authentication.wechat.api.WeChatApiImpl;
import com.sky.authentication.wechat.template.WeChatOAuth2Template;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class WeChatServiceProvider extends AbstractOAuth2ServiceProvider<IWeChatApi> {
    /**
     * 微信获取授权码的url
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
    /**
     * 微信获取accessToken的url
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";


    public WeChatServiceProvider(String appId, String appSecret) {
        super(new WeChatOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
    }


    @Override
    public IWeChatApi getApi(String accessToken) {
        return new WeChatApiImpl(accessToken);
    }
}
