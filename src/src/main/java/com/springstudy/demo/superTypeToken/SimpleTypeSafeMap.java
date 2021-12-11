package com.springstudy.demo.superTypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * THC(Typesafe Heterogenous Container) pattern
 */
public class SimpleTypeSafeMap {
    private Map<Class<?>,Object> map = new HashMap<>();

    public <T> void put(Class<?> k ,T v){
        map.put(k,v);
    }

    // Class<T> 의 cast 메서드를 통해 타입의 안정성을 확보한다
    public <T> T get(Class<T> k){
        return k.cast(map.get(k));
    }

    public int keySize(){
        return map.keySet().size();
    }

    public <T> boolean containsKey(Class<T> k){
        return map.containsKey(k);
    }
}
