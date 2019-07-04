package com.sky.demo.controller;

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
        return testService.test();
    }
}
