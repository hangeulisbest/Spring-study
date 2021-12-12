package com.springstudy.demo.superTypeToken;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@SpringBootTest
class SimpleTypeSafeMapTest {
    @Test
    public void type_token을_이용한_put_get_테스트() {
        SimpleTypeSafeMap simpleTypeSafeMap = new SimpleTypeSafeMap();
        simpleTypeSafeMap.put(String.class, "11st_문자열");
        simpleTypeSafeMap.put(Integer.class, 11);

        //타입 토큰을 이용해서 별도의 캐스팅 없이도 타입 안전성이 확보가능합니다.
        String v1 = simpleTypeSafeMap.get(String.class);
        Integer v2 = simpleTypeSafeMap.get(Integer.class);

        assertTrue(v1 instanceof String);
        assertTrue(v2 instanceof Integer);
    }

    @Test
    public void type_token_한계() {
        SimpleTypeSafeMap simpleTypeSafeMap = new SimpleTypeSafeMap();

        simpleTypeSafeMap.put(List.class, Arrays.asList(1,2,3));
        simpleTypeSafeMap.put(List.class, Arrays.asList("1", "2", "3"));

        List<Integer> v1 = (List<Integer>)simpleTypeSafeMap.get(List.class);
        List<String> v2 = (List<String>)simpleTypeSafeMap.get(List.class);

        // List.class 로 List<Integer>와 List<String>을 구분할 수 없다 따라서 슈퍼타입토큰이 필요!
        assertFalse(v1.get(0) instanceof Integer);
        assertTrue(v2.get(0) instanceof String);
    }

    @Test
    public void getGenericSuperClassTest(){
        class Super<T>{}
        class MyClass extends Super<List<String>>{};

        class Super2{}
        class MyClass2 extends Super2{}

        MyClass myClass = new MyClass();
        MyClass2 myClass2 = new MyClass2();

        Class<? extends MyClass> clazz1 = myClass.getClass();
        Class<? extends MyClass2> clazz2 = myClass2.getClass();

        System.out.println(clazz1); // ~$1MyClass
        System.out.println(clazz2); // ~$1MyClass2

        Type typeOfGenericSuperclass = clazz1.getGenericSuperclass();
        Type typeOfGenericSuperclass2 = clazz2.getGenericSuperclass();

        System.out.println(typeOfGenericSuperclass.getTypeName()); // Super<List<String>> 출력
        System.out.println(typeOfGenericSuperclass2.getTypeName()); //Super2 출력

        assertTrue(typeOfGenericSuperclass instanceof ParameterizedType); // Super 에는 List<String> 이라는 타입이 존재
        assertFalse(typeOfGenericSuperclass2 instanceof ParameterizedType);// Super2 에는 제네릭 파라미터타입이 없음

        Type actualTypeArguments = ((ParameterizedType) typeOfGenericSuperclass).getActualTypeArguments()[0];

        System.out.println(actualTypeArguments); // java.util.List<java.lang.String>


    }
}