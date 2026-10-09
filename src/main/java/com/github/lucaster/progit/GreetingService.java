package com.github.lucaster.progit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final String greeting;

    public GreetingService(@Value("${progit.greeting:Hello, World!}") String greeting) {
        this.greeting = greeting;
    }

    public String greet() {
        return greeting;
    }
}
