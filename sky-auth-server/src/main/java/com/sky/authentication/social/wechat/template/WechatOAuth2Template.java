package com.sky.authentication.social.wechat.template;

import com.sky.authentication.social.wechat.WechatAccessGrant;
import com.sky.authentication.social.wechat.entity.WechatResult;
import com.sky.authentication.social.wechat.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class WechatOAuth2Template extends OAuth2Template {
    private String clientId;
    private String clientSecret;
    private String accessTokenUrl;

    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";


    public WechatOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
    }

    /**
     * 获取access_token
     *
     * @param authorizationCode
     * @param redirectUri
     * @param parameters
     * @return
     */
    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri,
                                         MultiValueMap<String, String> parameters) {
        StringBuilder accessTokenRequestUrl = new StringBuilder(accessTokenUrl);

        accessTokenRequestUrl.append("?appid=").append(clientId);
        accessTokenRequestUrl.append("&secret=").append(clientSecret);
        accessTokenRequestUrl.append("&code=").append(authorizationCode);
        accessTokenRequestUrl.append("&grant_type=authorization_code");
        accessTokenRequestUrl.append("&redirect_uri=").append(redirectUri);

        return getAccessToken(accessTokenRequestUrl);
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {

        StringBuilder refreshTokenUrl = new StringBuilder(REFRESH_TOKEN_URL);

        refreshTokenUrl.append("?appid=").append(clientId);
        refreshTokenUrl.append("&grant_type=refresh_token");
        refreshTokenUrl.append("&refresh_token=").append(refreshToken);

        return getAccessToken(refreshTokenUrl);
    }

    private AccessGrant getAccessToken(StringBuilder accessTokenRequestUrl) {

        log.info("获取access_token, 请求URL: " + accessTokenRequestUrl.toString());

        //发送获取token
        String response = getRestTemplate().getForObject(accessTokenRequestUrl.toString(), String.class);

        log.info("获取access_token, 响应内容: " + response);

        WechatResult result;
        try {
            result = JsonUtils.getObjectMapper().readValue(response, WechatResult.class);
            if (StringUtils.isNotBlank(result.getErrcode())) {
                log.error("获取access token失败, errcode:" + result.getErrcode() + ", errmsg:" + result.getErrmsg());
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        WechatAccessGrant accessToken = new WechatAccessGrant(
                result.getAccess_token(),
                result.getScope(),
                result.getRefresh_token(),
                result.getExpires_in());

        accessToken.setOpenId(result.getOpenid());

        return accessToken;
    }

    /**
     * 构建获取授权码的请求。也就是引导用户跳转到微信的地址。
     */
    @Override
    public String buildAuthenticateUrl(OAuth2Parameters parameters) {
        parameters.setRedirectUri("http://www.dhwork.cn/social/wechat");
        return super.buildAuthenticateUrl(parameters) + "&appid=" + clientId + "&scope=snsapi_login";
    }

    @Override
    public String buildAuthorizeUrl(OAuth2Parameters parameters) {
        return buildAuthenticateUrl(parameters);
    }

    /**
     * 微信返回的contentType是html/text，添加相应的HttpMessageConverter来处理。
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}
