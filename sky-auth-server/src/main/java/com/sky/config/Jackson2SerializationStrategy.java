package com.sky.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

public class Jackson2SerializationStrategy extends StandardStringSerializationStrategy {
    private Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

    public Jackson2SerializationStrategy() {
        ObjectMapper mapper = new ObjectMapper();

//        mapper.registerModule(CoreJackson2Module());
//        mapper.registerModule(WebJackson2Module());
        mapper.addMixIn(DefaultOAuth2ClientContext.class, DefaultOAuth2ClientContextMixIn.class);

        serializer.setObjectMapper(mapper);
    }

    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        return (T) serializer.deserialize(bytes);
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        return serializer.serialize(object);
    }

    public Jackson2JsonRedisSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Jackson2JsonRedisSerializer serializer) {
        this.serializer = serializer;
    }


}

interface DefaultOAuth2ClientContextMixIn {

    @JsonTypeInfo(defaultImpl = DefaultOAuth2AccessToken.class, use = JsonTypeInfo.Id.NONE)
    OAuth2AccessToken getAccessToken();

    @JsonIgnore
    AccessTokenRequest getAccessTokenRequest();

}
