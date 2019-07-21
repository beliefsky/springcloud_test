package com.sky.authentication.social.wechat.provider;

import com.sky.authentication.social.wechat.api.IWechatApi;
import com.sky.authentication.social.wechat.api.WechatApiImpl;
import com.sky.authentication.social.wechat.template.WechatOAuth2Template;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class WechatServiceProvider extends AbstractOAuth2ServiceProvider<IWechatApi> {
    /**
     * 微信获取授权码的url
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
    /**
     * 微信获取accessToken的url
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";


    public WechatServiceProvider(String appId, String appSecret) {
        super(new WechatOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
    }


    @Override
    public IWechatApi getApi(String accessToken) {
        return new WechatApiImpl(accessToken);
    }
}
