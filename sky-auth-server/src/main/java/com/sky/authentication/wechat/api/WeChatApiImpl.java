package com.sky.authentication.wechat.api;

import com.sky.authentication.wechat.entity.WeChatUserInfo;
import com.sky.authentication.wechat.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

public class WeChatApiImpl extends AbstractOAuth2ApiBinding implements IWeChatApi {
    /**
     * 获取用户信息的url
     */
    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    public WeChatApiImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而微信返回的是UTF-8的，所以覆盖了原来的方法。
     */
//    @Override
//    protected List<HttpMessageConverter<?>> getMessageConverters() {
//        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
//        messageConverters.remove(0);
//        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
//        return messageConverters;
//    }
    @Override
    public WeChatUserInfo getUserInfo(String openId) {
        String url = URL_GET_USER_INFO + openId;
        String response = getRestTemplate().getForObject(url, String.class);
        if (StringUtils.contains(response, "errcode")) {
            return null;
        }
        try {
            return JsonUtils.getObjectMapper().readValue(response, WeChatUserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
