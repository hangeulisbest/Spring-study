package com.springstudy.demo.superTypeToken;

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
