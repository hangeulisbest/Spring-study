package com.springstudy.demo.annotation;

public class PrivateFieldObject {
    @MyAnnotation("Hello World")
    private String name;

    public PrivateFieldObject() {
    }

    public PrivateFieldObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
