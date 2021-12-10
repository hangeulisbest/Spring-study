package com.springstudy.demo.superTypeToken;
import java.util.ArrayList;
import java.util.List;

public class NonGenericListSample {
    public List list = new ArrayList();

    public void add(Object o){
        list.add(o);
    }

    public String getString(int i){
        return (String) list.get(i);
    }
}
