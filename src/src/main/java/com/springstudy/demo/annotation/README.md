## Custom Annotaion 만들기

[출처](https://elfinlas.github.io/2017/12/14/java-annotation/)

#### Annotation이란?
```
JEE5 부터 새로 추가된 요소이다. 어노테이션을 이용해 코드의 깔끔함과 유효성검사등을 쉽게 할수 있다.
소스코드의 메타데이터를 표현하기위해 많이 사용한다.
```

#### 자바에서 기본적으로 제공하는 어노테이션
```
@Override
선언한 메서드가 오버라이드 되었다는 것을 나타냅니다.
만약 상위(부모) 클래스(또는 인터페이스)에서 해당 메서드를 찾을 수 없다면 컴파일 에러를 발생 시킵니다.

@Deprecated
해당 메서드가 더 이상 사용되지 않음을 표시합니다.
만약 사용할 경우 컴파일 경고를 발생 키십니다.

@SuppressWarnings
선언한 곳의 컴파일 경고를 무시하도록 합니다.

@SafeVarargs
Java7 부터 지원하며, 제너릭 같은 가변인자의 매개변수를 사용할 때의 경고를 무시합니다.

@FunctionalInterface
Java8 부터 지원하며, 함수형 인터페이스를 지정하는 어노테이션입니다.
만약 메서드가 존재하지 않거나, 1개 이상의 메서드(default 메서드 제외)가 존재할 경우 컴파일 오류를 발생 시킵니다.
```

#### Meta-Annotation 종류
```
@Retention
자바 컴파일러가 어노테이션을 다루는 방법을 기술하며, 특정 시점까지 영향을 미치는지를 결정합니다.
종류는 다음과 같습니다.
RetentionPolicy.SOURCE : 컴파일 전까지만 유효. (컴파일 이후에는 사라짐)
RetentionPolicy.CLASS : 컴파일러가 클래스를 참조할 때까지 유효.
RetentionPolicy.RUNTIME : 컴파일 이후에도 JVM에 의해 계속 참조가 가능. (리플렉션 사용)

@Target
어노테이션이 적용할 위치를 선택합니다.
종류는 다음과 같습니다.
- ElementType.PACKAGE : 패키지 선언
- ElementType.TYPE : 타입 선언 (class,interface)
- ElementType.ANNOTATION_TYPE : 어노테이션 타입 선언
- ElementType.CONSTRUCTOR : 생성자 선언
- ElementType.FIELD : 멤버 변수 선언
- ElementType.LOCAL_VARIABLE : 지역 변수 선언
- ElementType.METHOD : 메서드 선언
- ElementType.PARAMETER : 전달인자 선언
- ElementType.TYPE_PARAMETER : 전달인자 타입 선언
- ElementType.TYPE_USE : 타입 선언

@Documented
해당 어노테이션을 Javadoc에 포함시킵니다.

@Inherited
어노테이션의 상속을 가능하게 합니다.

@Repeatable
Java8 부터 지원하며, 연속적으로 어노테이션을 선언할 수 있게 해줍니다.
```

#### MyAnnotation 정의하기

MyAnnotation은 String 필드에 선언하 "MyAnnotation Default Value"라는 값을 할당합니다. 다른 필드에 선언되어있다면 예외를 던질것이고 해당 필드가 private이여도 할 수 있도록 할겁니다. 이제부터 예제를 살펴보면서 Annotation이 적용되는 과정을 테스트 해볼겁니다. 
<br><br>
MyAnnotation을 아래와 같이 정의합니다.
```java
// Field를 타겟으로 한다
// RUNTIME에도 참조 할 수 있도록 한다.
// Document에도 기록한다.
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAnnotation {
    String value() default "MyAnnotation Default Value";
}
```

이제 Annotation을 선언할 클래스를 선언합니다.

```java
import lombok.ToString;

@ToString
public class MyObject {

    @MyAnnotation
    String name;

    public MyObject() {
    }

    public MyObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

MyObject에 name에는 "MyAnnotation Default Value" 값이 기본적으로 세팅되도록 하려고 합니다. <br>
기본값이 세팅되도록 하기위해 AnnotationUtil 을 만들면 아래와 같이 적용되길 원합니다. <br>

```java
public class test{
    private static void main(){
        // obj.name = MyAnnotation Default Value
        MyObject obj = AnnotationUtil.getNameDefaultObj(MyObject.class);
    }
}
```

이제부터 가장 중요한 AnnotationUtil을 작성해봅시다. <br> 
일단 작성해보고 설명하겠습니다. <br>

```java
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class AnnotationUtil {
    /**
     *
     * name MyAnnotation이 붙은 모든 String 필드에 대해 Default String을 넣는 함수
     * @param clazz
     * @return
     * @throws IllegalAccessException
     */
    public static <T> T getNameDefaultObj(Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        try {
            Field[] fields = clazz.getDeclaredFields();
            Constructor<?> defaultConstructer = clazz.getDeclaredConstructors()[0];
            Object o = defaultConstructer.newInstance();

            for(Field f : fields){
                if(f.isAnnotationPresent(MyAnnotation.class)){
                    MyAnnotation annotation = f.getAnnotation(MyAnnotation.class);
                    if(!f.getType().equals(String.class)){
                        throw new IllegalArgumentException("String 필드에만 어노테이션을 붙일 수 있습니다.");
                    }
                    // private field 도 접근 가능함
                    f.setAccessible(true);
                    f.set(o,annotation.value());
                }
            }
            return clazz.cast(o);
        }catch (InstantiationException e){
            throw new InstantiationException("초기화 오류");
        }catch (InvocationTargetException e){
            throw new InvocationTargetException(new Throwable("reflect 오류!"));
        }
    }
}

```

1) class<T> 타입 토큰을 이용해 파라미터 clazz 를 받습니다. <br>
2) clazz에 선언된 필드들을 가져옵니다. (getDeclaredFields) <br>
3) clazz의 기본생성자를 통해서 객체를 생성합니다. (defaultConstructer.newInstance()) <br>
4) 필드들을 순회하면서 @MyAnnotation 이 선언된 필드를 찾습니다. 
5) @MyAnnotation 이 선언되어 있는 필드의 타입이 String이 아니면 예외를 반환합니다.
6) 선언된 필드의 private,public 여부와 관계없이 접근 가능하도록 설정합니다.
7) 필드의 값을 MyAnnotation의 기본 value() 를 세팅합니다.
8) 세팅된 객체를 clazz의 cast()함수를 이용해 변환합니다.

이제 테스트하는 코드입니다. <br>
위 내용에 없는 NotMyObject , IntFieldObject , PrivateFieldObject 는 소스코드를 확인해주세요 <br>
```java

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
```


#### MyAnnotation 테스트코드
- [**AnnotationUtilTest**](../../../../../../test/java/com/springstudy/demo/annotation/AnnotationUtilTest.java)