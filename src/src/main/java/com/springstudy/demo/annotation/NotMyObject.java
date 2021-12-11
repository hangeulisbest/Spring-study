package com.springstudy.demo.annotation;

import lombok.ToString;

@ToString
public class NotMyObject {

    @MyAnnotation
    String noName;

    public NotMyObject() {
    }

    public NotMyObject(String noName) {
        this.noName = noName;
    }

    public String getNoName() {
        return noName;
    }

    public void setNoName(String noName) {
        this.noName = noName;
    }
}
