## 슈퍼 타입 토큰(SuperTypeToken)
[**출처**](https://sungminhong.github.io/spring/superTypeToken/)

----

- **목차**

| 주제 | 
|:---|
|Generic Type Erasure|
|Generic의 필요성을 위한 코드|
|Type Token 은 무엇인가? (feat. 클래스 리터럴) |
|Type Token 예시|


---
#### Generic Type Erasure

```text
Erasure(소거)란 원소타입을 컴파일시 검사하고 런타임에는 타입의 정보를 알 수 없다.
런타임에 타입을 제거하는 것을 의미한다.
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

#### Type Token이란?
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
##### ㄴ 슈퍼타입토큰이 필요한 이유
- [SimpleTypeSafeMapTest](../../../../../../test/java/com/springstudy/demo/superTypeToken/SimpleTypeSafeMapTest.java)

