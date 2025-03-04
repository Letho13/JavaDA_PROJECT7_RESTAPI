package com.example.demo;

import com.nnk.springboot.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = {"com.nnk.springboot"})
public class DemoApplication implements CommandLineRunner {

    private final UserService userService;

    public DemoApplication(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        userService.createTemporaryUser();
        userService.createTemporaryAdmin();
    }

}
