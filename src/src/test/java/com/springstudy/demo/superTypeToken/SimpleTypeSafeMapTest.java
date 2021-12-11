package com.springstudy.demo.superTypeToken;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
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
        assertTrue(simpleTypeSafeMap.containsKey(List.class));
        List<Integer> vv1 = (List<Integer>)simpleTypeSafeMap.get(List.class);
        assertEquals(vv1.get(0),1);

        simpleTypeSafeMap.put(List.class, Arrays.asList("1", "2", "3"));
        List<Integer> vv2 = (List<Integer>)simpleTypeSafeMap.get(List.class);

        // 문자열 리스트를 List<Integer>로 캐스팅하면 컴파일 오류가 안남! 런타임도 안남! 그래서 슈퍼타입토큰 생김
        List<Integer> temp =  List.class.cast(Arrays.asList("1","2","3"));
        assertFalse(temp.get(0) instanceof Integer);


        assertEquals(vv2.get(0),"1");


        List<Integer> v1 = (List<Integer>)simpleTypeSafeMap.get(List.class);
        List<String> v2 = (List<String>)simpleTypeSafeMap.get(List.class);

        // List.class 로 List<Integer>와 List<String>을 구분할 수 없다 따라서 슈퍼타입토큰이 필요!
        assertFalse(v1.get(0) instanceof Integer);
        assertTrue(v2.get(0) instanceof String);
    }
}