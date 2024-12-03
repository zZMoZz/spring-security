package com.zzz.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/*
 1. we put "/error" in permitted requests, to make error accessible by anyone.
 */


@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/hello1", "/hello2").authenticated()
                        .requestMatchers("/hello3", "/hello4", "/error", "/register").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .build();
    }

    /*
    I add this bean because in "AuthController" class hey say I can't
    autowired PasswordEncoder as no bean of its type. You may say but
    there is passwordEncoder where It used in login operation. You alright
    but the Authentication provider creates an instance of it locally it is
    not a bean, he create an instance of one of implementations of "PasswordEncode"
    then assign it to a variable of "PasswordEncode" and use it. we will make same as
    Authentication provider but make this setup as a bean to can use by other components
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
