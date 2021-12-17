package com.springstudy.demo.superTypeToken;

import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TypeSafeMap {
    private final Map<Type,Object> map = new HashMap<>();

    public <T> void put(TypeReference<T> tr,T v){
        map.put(tr.getType(),v);
    }

    public <T> T get(TypeReference<T> tr){
        final Type type = tr.getType();
        final Class<T> clazz;

        if(type instanceof ParameterizedType){
            // getRawType 은 제네릭을 모두 제거한 타입을 가져옵니다. 예를 들어 List<String> 이라면 List.class 를 가져옴
            // 또 예를 들면 List<HashMap<String,String>> 이라도 RawType 은 List.class로 가져옴
            // 즉, 파라미터로 쓰는 타입의 RAW타입을 가져옵니다.
            clazz =(Class<T>)((ParameterizedType) type).getRawType();
        }else{
            // Class 는 인터페이스 Type 을 구현한 클래스임
            // 파라미터로 쓰는 타입이 없다면 그냥 캐스팅함
            clazz = (Class<T>) type;
        }

        // clazz 는 T 제네릭 파라미터를 가지고 있고
        // 쉽게 말하면 T.class.cast(T); 로 비슷하게 이해할 수 있다.
        // 예를 들어 String.class.cast("HELLO");
        // List.class.cast(Arrays.asList("1","2","3"));
        return clazz.cast(map.get(tr.getType()));

    }
}
