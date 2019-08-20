package com.sky.demo.controller;

import com.sky.demo.entity.Test;
import com.sky.demo.entity.TestModel;
import com.sky.demo.service.TestService;
import com.sky.utils.ExcelTemplateUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.util.Arrays;

@RestController
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping("/file")
    public String file() throws IOException {

        Test[] tests = {
                new Test(1, "test1", "男"),
                new Test(2, "test2", "女"),
                new Test(3, "test3", "男"),
                new Test(4, "test4", "男"),
                new Test(5, "test5", "女"),
                new Test(6, "test6", "男"),
        };

        TestModel model = new TestModel();
        model.setList(Arrays.asList(tests));

        String filePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()
                + "static/excel/template.xlsx";

        OutputStream outputStream = new FileOutputStream("/home/weijing/test.xlsx");
        ExcelTemplateUtils.process(filePath, model, outputStream);
        outputStream.flush();
        outputStream.close();
        return filePath;
    }

    @GetMapping("/test")
    public String test() {
//        String result = HttpConnectionPoolUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx4a11ae335e843db1&secret=09069ff8aa678e0262b397b65dd0d43e&code=001uPrcU04M8TY1XbT8U0yS1cU0uPrc6&grant_type=authorization_code");
//        System.out.println(result);
        return testService.test();
    }
}
