package com.springstudy.demo.annotation;

public class IntFieldObject {

    @MyAnnotation
    int name;

    public IntFieldObject() {
    }

    public IntFieldObject(int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
