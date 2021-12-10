package com.springstudy.demo.superTypeToken;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NonGenericListSampleTest {

    @Test
    public void list에_스트링삽입하고_가져오기_테스트(){
        NonGenericListSample s = new NonGenericListSample();
        s.list.add("Hello world");
        String find = s.getString(0);
        assertEquals("Hello world",find);
    }
    @Test
    public void int형은_String으로_캐스팅이되지않는다(){
        int a = 1;
        // String s = (String) a; complie Error
    }

    @Test
    public void list에_integer_삽입하고_가져오_ClassCastException(){
        NonGenericListSample s = new NonGenericListSample();
        // compile때 오류잡지 못함
        s.add(1);


        // s.list.get(0) 는 int형인데 (String)으로 캐스팅하여 예외발생
        assertThrows(ClassCastException.class,()->{
           s.getString(0);
        });

    }

}