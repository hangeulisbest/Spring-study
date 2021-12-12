package com.springstudy.demo.superTypeToken;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TypeSafeMapTest {

    private final TypeSafeMap map = new TypeSafeMap();

    @Test
    public void 문자열_타입안정성_테스트(){
        final String inData = "문자열 테스트";
        // 추상클래스의 익명의 서브클래스로 인스턴스화
        final TypeReference<String> tr = new TypeReference<String>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };

        // put test
        map.put(tr,inData);

        // get test
        final String outData = map.get(tr);

        assertEquals(inData,outData);

    }

    @Test
    public void List_타입안정성_테스트(){
        final List<String> inData = Arrays.asList("1","2","3");

        final TypeReference<List<String>> tr = new TypeReference<List<String>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };

        System.out.println(tr.getType());

        //put
        map.put(tr,inData);

        //get
        final List<String> outData = map.get(tr);

        assertEquals(inData,outData);
    }

    @Test
    public void put_and_get_test_using_list_list_string() {
        final List<List<String>> inputData = Arrays.asList(
                Arrays.asList("H", "O", "N", "G"),
                Arrays.asList("S", "U", "N", "G"),
                Arrays.asList("M", "I", "N")
        );
        // List<List<String>>.class와 동일한 효과
        final TypeReference<List<List<String>>> tr= new TypeReference<>() {};

        // put
        map.put(tr, inputData);
        // get
        final List<List<String>> outputData = map.get(tr);

        System.out.println("input: " + inputData + "\n" +  "output: " +outputData);
        assertEquals(inputData, outputData);
    }

}