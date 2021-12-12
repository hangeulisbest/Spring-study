## ParameterizedTypeReference 구현하기
[**출처**](https://sungminhong.github.io/spring/superTypeToken/)

----

- **목차**

| 주제 | 
|:---|
|Generic Type Erasure|
|Generic의 필요성을 위한 코드|
|Type Token 은 무엇인가? (feat. 클래스 리터럴) |
|Type Safe한 Map 만들기|



---
#### Generic Type Erasure

```text
Erasure(소거)란 원소타입을 컴파일시 검사하고 런타임에는 타입의 정보를 알 수 없습니다.
런타임에 타입을 제거하는 것을 의미합니다.
```
#####  ㄴ unbound type example
```java
// 컴파일 할 때 (타입 소거 전) 
public class Test<T> {
    public void test(T test) {
        System.out.println(test.toString());
    }
}
// 런타임 때 (타입 소거 후)
public class Test {
    public void test(Object test) {
        System.out.println(test.toString());
    }
}
```
#### Generic의 필요성을 위한 코드

- [NonGemericListSample](./NonGenericListSample.java)
- [NonGemericListSampleTest](../../../../../../test/java/com/springstudy/demo/superTypeToken/NonGenericListSampleTest.java)

#### Type Token 은 무엇인가? (feat. 클래스 리터럴) 
- 클래스 리터럴 = XXX.class
- 타입토큰 = Class<XXX>

```java
/*
    JDK5시절 java.lang.Class 가 generic Type이 되도록 변경했다.
    예를 들어, String.class 를 Class<String> 으로 받을 수 있도록 만들었다.
*/

// 아래와 같은 메서드는 타입 토큰을 인자로 받는 메서드입니다.
void myMethod(Class<?> clazz) {
  ...
}
  
// String.class라는 클래스 리터럴을 타입 토큰 파라미터로 myMethod에 전달합니다.
myMethod(String.class);
```

##### Type Safe한 Map 만들기
Type Token을 이용해 타입안정성있는 Map을 만들어보려고 합니다. <br>
Map은 <Class<?>,Object> 형입니다. 
예를 들어 String.class 를 Key 로 가지고 있는 Map의 Value 역시 String.class 입니다. <br>
과연 타입안정성을 보장하는지 확인해봅니다.

```java
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
```

테스트 해봅시다.

```java
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
}
```

#### Type Token의 한계

- [SimpleTypeSafeMapTest 소스코드 전체보기](../../../../../../test/java/com/springstudy/demo/superTypeToken/SimpleTypeSafeMapTest.java)

String.class 와 Integer.class에 대해서는 잘 동작합니다. <br>
하지만 List<String> 과 List<Integer> 에 대해서는 동작하지 않습니다. <br>
아래의 코드를 보면서 설명하겠습니다.

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

@SpringBootTest
class SimpleTypeSafeMapTest {
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
}
```

List<Integer> 와 List<String>은 List<String>.class 와 List<Integer>.class <br>
와 같은 클래스 리터럴이 존재하지 않습니다. 그래서 타입이레이저를 통해 타입토큰을 사용할수 없습니다.

#### Super Type Token 이란?

수퍼급의 타입토큰이 아니라 수퍼타입의 토큰을 사용하겠다는 의미입니다. <br>
수퍼 타입 토큰은 **상속**과 **reflection**을 사용하여 List<String>.class 와 같이 사용하는것과 같은 원래 사용할 수 없었던 클래스 리터럴을 타입토큰으로 사용하는것과 같은 효과를 냅니다.
<br> <br>
String.class의 타입은 Class<String> 입니다. <br>
Class<String> 타입토큰을 통해 String.class 라는 타입을 받을 수 있도록 하여 타입안정성을 확보했습니다. <br>
만약에 Class<List<String>> 이라는 토큰을 이용해 List<String>.class 라는 타입을 받을 수 있다면 어떨까요?? <br>
<br>

그렇다면 List 내에 중첩된 타입은 어떻게 구할 수 있을까요?? <br> <br>

- **결론**: Class.getGenericSuperclass()와 ParameterizedType.getActualTypeArguments()를 사용 <br>


#### getGenericSuperclass

getGenericSuperclass 가 어떤 기능인지 한번 사용해봅시다. <br> 

```java

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

    }
}
```

이처럼 getGenericSuperClass 는 상위 클래스의 타입을 가져옵니다. <br>
만약 상위 클래스의 파라미터 타입이 있다면 함께 가져옵니다. (ex. Super\<List\<String\>\>) <br>

<br>

이제 상위 클래스의 타입의 제네릭의 실제타입을 가져와 봅시다. <br>
즉 getActualTypeArguments 를 사용해볼겁니다. <br> 

```java
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
```

typeOfGenericSuperclass 는  ParameterizedType 이므로 캐스팅이 가능합니다. <br> 
아래와 같은 코드는 에러가 납니다.

```java
Type ClassCastException = ((ParameterizedType) typeOfGenericSuperclass2).getActualTypeArguments()[0];
```

다시 돌아와서 상위 클래스의 파라미터 실제 타입을 가져오는데 성공했습니다.



