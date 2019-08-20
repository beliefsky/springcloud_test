package com.sky.demo.entity;

import lombok.Data;

@Data
public class Test {
    private Integer id;
    private String name;
    private String sex;

    public Test(Integer id, String name, String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }
}
