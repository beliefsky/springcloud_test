package com.sky.authentication.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sky.authentication.social.wechat.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocialConnectionSignUp implements ConnectionSignUp {


    @Override
    public String execute(Connection<?> connection) {

        //根据社交用户信息默认创建用户并返回用户唯一标识,当不用@Component时，就用

        //这时候，偷偷给用户添加一条user表，并且返回用户的uin

        //业务需要1
        //当用户直接用QQ登录的时候，不需要提示用户注册，后台直接注册给用户注册
//        String uin = RandomUtil.randomString(6);

//        UserModel userModel = new UserModel(Long.parseLong(uin), connection.getDisplayName(), "123456", null);
        //在微服务中，最好是RPC调用
//        jpaRepository.save(userModel);

        //业务需求2
        //当改用户第一次注册没手机号，就提示用手机号绑定

        log.info("==============================");
        try {
            log.info(JsonUtils.getObjectMapper().writeValueAsString(connection));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return connection.getDisplayName();
    }
}
