package com.nnk.springboot.configuration;

import com.nnk.springboot.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserService userService) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
//        return authenticationManagerBuilder.build();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(UserService userService, PasswordEncoder passwordEncoder) throws Exception {
//        return new ProviderManager(List.of(new DaoAuthenticationProvider() {{
//            setUserDetailsService(userService);
//            setPasswordEncoder(passwordEncoder);
//        }}));
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Chargement de SecurityConfig...");
        System.out.println("Chargement de SecurityConfig...");
        return http
                .cors(withDefaults())
                .csrf(withDefaults())
//                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/login", "/css/**", "/js/**", "/images/**")
//                            .permitAll();
//                    auth.requestMatchers("/register")
//                            .hasAuthority("ADMIN");
//                    auth.anyRequest()
//                            .hasAnyAuthority("USER", "ADMIN");
//                })

                .formLogin(form -> {
                    form.loginPage("/login")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .successHandler(new CustomAuthentificationSuccessHandler())
                            .failureUrl("/login?error")
                            .permitAll();
                })

//                .logout(logout -> {
//                    logout.logoutUrl("/logout");
//                    logout.logoutSuccessUrl("/login?logout");
//                    logout.invalidateHttpSession(true);
//                    logout.deleteCookies("JSESSIONID");
//                })
//                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
//                    httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/accessDenied");
//
//                })
                .build();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }
//}
//
//
//@PostConstruct
//public void init() {
//    log.info("SecurityConfig a bien été chargée !");
//    System.out.println("SecurityConfig a bien été chargée !");
//}
}
