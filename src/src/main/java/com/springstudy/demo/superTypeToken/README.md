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
하지만 List\<String\> 과 List\<Integer\> 에 대해서는 동작하지 않습니다. <br>
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

List\<Integer\> 와 List\<String\>은 List\<String\>.class 와 List\<Integer\>.class <br>
와 같은 클래스 리터럴이 존재하지 않습니다. 그래서 타입이레이저를 통해 타입토큰을 사용할수 없습니다.

#### Super Type Token 이란?

수퍼급의 타입토큰이 아니라 수퍼타입의 토큰을 사용하겠다는 의미입니다. <br>
수퍼 타입 토큰은 **상속**과 **reflection**을 사용하여 List<String>.class 와 같이 사용하는것과 같은 원래 사용할 수 없었던 클래스 리터럴을 타입토큰으로 사용하는것과 같은 효과를 냅니다.
<br> <br>
String.class의 타입은 Class\<String\> 입니다. <br>
Class\<String\> 타입토큰을 통해 String.class 라는 타입을 받을 수 있도록 하여 타입안정성을 확보했습니다. <br>
만약에 Class\<List\<String\>\> 이라는 토큰을 이용해 List\<String\>.class 라는 타입을 받을 수 있다면 어떨까요?? <br>
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

##### SimpleTypeSafeMapTest 소스코드

- [SimpleTypeSafeMapTest](../../../../../../test/java/com/springstudy/demo/superTypeToken/SimpleTypeSafeMapTest.java)


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
// typeOfGenericSuperclass2 는 RawType 그 자체라서 ParameterizedType으로 캐스팅할 수 없다.
Type ClassCastException = ((ParameterizedType) typeOfGenericSuperclass2).getActualTypeArguments()[0];
```

다시 돌아와서 상위 클래스의 파라미터 실제 타입을 가져오는데 성공했습니다.
위 처럼 실제타입을 가져오기 위해서는 상위클래스의 파라미터로 한번 감싼 다음에 가져와야 합니다.
이를 이용해 타입에 안전한 맵을 만들어 보겠습니다.

```java
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

```


이렇게 만든 타입 안전한 맵을 테스트 해봅니다.

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.ParameterizedType;
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

        System.out.println("tr.getType() : " + tr.getType());

        // put
        map.put(tr, inputData);

        // get
        final List<List<String>> outputData = map.get(tr);

        System.out.println("input: " + inputData + "\n" +  "output: " +outputData);
        assertEquals(inputData, outputData);
    }

}
```

##### TypeSafeMapTest 소스코드 보기
- [TypeSafeMapTest](../../../../../../test/java/com/springstudy/demo/superTypeToken/TypeSafeMapTest.java)



위 처럼 TypeReference\<T\> 를 이용해 T의 타입토큰을 구해보았습니다. <br>
T의 타입 토큰을 구하기 위해서 상위 클래스인 TypeReference의 타입파라미터인 T를 가져와서 구한것을 다시 말하면 <br>
슈퍼클래스의 타입토큰을 줄여서 슈퍼타입토큰이라고 합니다. <br>
위 내용을 스프링에서는 ParameterizedTypeReference 라는 이름으로 구현하고 있는데 아래에 있습니다.<br>


```java
public abstract class ParameterizedTypeReference<T> {

	private final Type type;


	protected ParameterizedTypeReference() {
		Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
		Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
		Assert.isInstanceOf(ParameterizedType.class, type, "Type must be a parameterized type");
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		Assert.isTrue(actualTypeArguments.length == 1, "Number of type arguments must be 1");
		this.type = actualTypeArguments[0];
	}

	private ParameterizedTypeReference(Type type) {
		this.type = type;
	}


	public Type getType() {
		return this.type;
	}

	@Override
	public boolean equals(@Nullable Object other) {
		return (this == other || (other instanceof ParameterizedTypeReference &&
				this.type.equals(((ParameterizedTypeReference<?>) other).type)));
	}

	@Override
	public int hashCode() {
		return this.type.hashCode();
	}

	@Override
	public String toString() {
		return "ParameterizedTypeReference<" + this.type + ">";
	}


	/**
	 * Build a {@code ParameterizedTypeReference} wrapping the given type.
	 * @param type a generic type (possibly obtained via reflection,
	 * e.g. from {@link java.lang.reflect.Method#getGenericReturnType()})
	 * @return a corresponding reference which may be passed into
	 * {@code ParameterizedTypeReference}-accepting methods
	 * @since 4.3.12
	 */
	public static <T> ParameterizedTypeReference<T> forType(Type type) {
		return new ParameterizedTypeReference<T>(type) {
		};
	}

	private static Class<?> findParameterizedTypeReferenceSubclass(Class<?> child) {
		Class<?> parent = child.getSuperclass();
		if (Object.class == parent) {
			throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
		}
		else if (ParameterizedTypeReference.class == parent) {
			return child;
		}
		else {
			return findParameterizedTypeReferenceSubclass(parent);
		}
	}

}
```

위 ParameterizedTypeReference 를 이용해서 어떻게 사용하는지 실습해보겠습니다.
컨트롤러 클래스를 먼저 만들고 테스트해보겠습니다. 아래는 컨트롤러 입니다.
```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ParameterizedTypeReferenceTestController {

    @GetMapping("/users")
    public List<TestUser> getUsers(){
        return Arrays.asList(
                new TestUser("candy",10)
                ,new TestUser("dog",2)
                ,new TestUser("cat",3));
    }

    @Data
    @AllArgsConstructor
    @ToString
    static class TestUser{
        private String name;
        private int age;
    }


}

```

이제 테스트 해보겠습니다.

```java
package com.springstudy.demo.superTypeToken;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.springstudy.demo.superTypeToken.ParameterizedTypeReferenceTestController.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParameterizedTypeReferenceTestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 슈퍼타입_없이_호출해보기(){
        List<TestUser> ret = restTemplate.getForObject("/users", List.class);
        final Object first = ret.get(0);

        assertFalse(first instanceof TestUser);
        assertTrue(first instanceof LinkedHashMap);
        System.out.println(first); // {name=candy, age=10}
    }

    @Test
    public void 슈퍼타입으로_호출해보기(){
        List<TestUser> ret = restTemplate.exchange("/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TestUser>>() {
                }
        ).getBody();

        final Object first = ret.get(0);

        assertTrue(first instanceof TestUser);
    }
}
```

##### ParameterizedTypeReferenceTestControllerTest 소스코드 보기 
- [ParameterizedTypeReferenceTestControllerTest](../../../../../../test/java/com/springstudy/demo/superTypeToken/ParameterizedTypeReferenceTestControllerTest.java)
