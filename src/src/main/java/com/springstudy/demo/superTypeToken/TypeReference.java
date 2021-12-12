package com.springstudy.demo.superTypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> {
    private final Type type;

    protected TypeReference() {
        // 자식 객체에서 호출된다면 T의 정보를 가져옴
        Type superTypeClass = this.getClass().getGenericSuperclass();

        // T의 타입을 가져온다
        if(superTypeClass instanceof ParameterizedType){
            this.type = ((ParameterizedType) superTypeClass).getActualTypeArguments()[0];
        }
        else {
            throw new IllegalArgumentException("TypeReference는 항상 실제 타입 파라미터 정보가 있어야 합니다.");
        }
    }

    public Type getType(){
        return this.type;
    }
}
