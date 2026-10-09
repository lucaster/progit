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
        if (args.length > 0) {
            System.out.println(greetingService.greetTo(args[0]));
        } else {
            System.out.println(greetingService.greet());
        }
    }
}
