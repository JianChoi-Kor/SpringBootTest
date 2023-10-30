package com.example.board.springboottest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest(properties = {"key1=value1", "key2=value2"},
        args = {"--secret=exampleKey"},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class BoardApplication2Tests {

    @Value("${key1}")
    private String key1;

    @Value("${key2}")
    private String key2;

    @Autowired
    private ApplicationArguments args;

    @BeforeEach
    void before() {
        System.out.println("key1: " + key1);
        System.out.println("key2: " + key2);
        String secret = args.getOptionValues("secret").get(0);
        System.out.println("secret: " + secret);
    }

    @Test
    void test() {

    }
}
