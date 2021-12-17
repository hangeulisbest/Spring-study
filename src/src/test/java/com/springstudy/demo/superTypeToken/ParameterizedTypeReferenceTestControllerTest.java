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