package com.springstudy.demo.annotation;

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
