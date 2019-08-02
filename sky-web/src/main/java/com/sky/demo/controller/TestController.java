package com.sky.demo.controller;

import com.sky.demo.HttpConnectionPoolUtil;
import com.sky.demo.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private TestService testService;


    @GetMapping("/test")
    public String test() {
        String result = HttpConnectionPoolUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx4a11ae335e843db1&secret=09069ff8aa678e0262b397b65dd0d43e&code=001uPrcU04M8TY1XbT8U0yS1cU0uPrc6&grant_type=authorization_code");
        System.out.println(result);
        return testService.test();
    }
}
