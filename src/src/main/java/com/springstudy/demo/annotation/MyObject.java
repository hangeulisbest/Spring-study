package com.springstudy.demo.annotation;

import lombok.ToString;

@ToString
public class MyObject {

    @MyAnnotation
    String name;

    public MyObject() {
    }

    public MyObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
