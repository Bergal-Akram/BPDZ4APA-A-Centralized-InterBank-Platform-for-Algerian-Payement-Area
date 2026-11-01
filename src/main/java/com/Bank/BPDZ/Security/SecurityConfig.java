package com.Bank.BPDZ.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // For form login without CSRF token (not recommended for production)
            //this is a part of authorization
            .authorizeHttpRequests(auth -> auth
            		//permi all the request "login"and "logout"
                .requestMatchers("/login", "/logout", "/css/**", "/js/**").permitAll()
                .anyRequest().permitAll() // You can restrict this later with roles
            )
            .formLogin().disable(); // Disable Spring Security's default login form (ask me)

        return http.build();
    }
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
