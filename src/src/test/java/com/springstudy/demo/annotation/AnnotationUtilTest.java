package com.springstudy.demo.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnnotationUtilTest {

    @Test
    public void MyObject에_나의어노테이션_적용하기() throws Exception{

        // MyObject라는 클래스의 어노테이션을 가져온다.
        MyAnnotation annotation = MyObject.class
                .getDeclaredField("name")
                .getAnnotation(MyAnnotation.class);

        // MyObject클래스의 객체를 가져온다.
        MyObject obj = AnnotationUtil.getNameDefaultObj(MyObject.class);

        // MyAnnotation이 붙어있는 필드인 name은 기본적으로 MyAnnotation Default Value 라는 값을 가지고 있다.
        assertEquals(obj.getName(),annotation.value());

        // NotMyObject 에서도 어노테이션이 붙은 모든 필드에 디폴트값을 저장한다
        NotMyObject obj2 = AnnotationUtil.getNameDefaultObj(NotMyObject.class);
        assertEquals(obj2.getNoName(),annotation.value());

    }

    @Test
    public void int형에는_MyAnnotaion을_붙일수_없다() throws Exception{
        //NotMyObject 의 money에 붙은 annotation을 가져온다
        MyAnnotation annotation = IntFieldObject.class
                .getDeclaredField("name")
                .getDeclaredAnnotation(MyAnnotation.class);

        // int형에 붙인값을 적용하면 오류!
        assertThrows(IllegalArgumentException.class,()->{
           AnnotationUtil.getNameDefaultObj(IntFieldObject.class);
        });
    }

    @Test
    public void Private필드에_접근할수있다() throws Exception{
        MyAnnotation annotation = PrivateFieldObject.class
                .getDeclaredField("name")
                .getDeclaredAnnotation(MyAnnotation.class);

        // PrivateFieldObject 는 private 필드를 가지고 있다.
        PrivateFieldObject obj = AnnotationUtil.getNameDefaultObj(PrivateFieldObject.class);

        assertEquals(obj.getName(),annotation.value());

    }

}