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

이제부터 예제를 살펴보면서 Annotation이 적용되는 과정을 테스트 해볼겁니다. 
<br>
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


#### MyAnnotation 테스트코드
- [**AnnotationUtilTest**](../../../../../../test/java/com/springstudy/demo/annotation/AnnotationUtilTest.java)