package com.github.lucaster.progit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GreetingRunner implements CommandLineRunner {

    private final GreetingService greetingService;

    public GreetingRunner(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Override
    public void run(String... args) {
        System.out.println(greetingService.greet());
    }
}
