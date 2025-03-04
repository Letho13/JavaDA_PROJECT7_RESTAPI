package com.nnk.springboot.configuration;

import com.nnk.springboot.repositories.UserRepository;

import com.nnk.springboot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserServiceConfig {


    @Bean
    public UserDetailsService userDetails(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        return new UserService(userRepository,passwordEncoder);
    }

}
