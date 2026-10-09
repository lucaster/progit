package com.github.lucaster.progit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    void greetsWithTheDefaultMessage() {
        assertThat(greetingService.greet()).isEqualTo("Hello, World!");
    }

    @Test
    void greetsAPersonByName() {
        assertThat(greetingService.greetTo("Luca")).isEqualTo("Hello, Luca!");
    }
}
