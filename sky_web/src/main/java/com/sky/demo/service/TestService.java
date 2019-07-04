package com.sky.demo.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("demo-server")
public interface TestService {

    @GetMapping("/hello")
    String test();
}
