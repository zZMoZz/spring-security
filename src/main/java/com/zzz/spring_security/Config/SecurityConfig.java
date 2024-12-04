package com.zzz.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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
    1. Of course, we use "DelegatingPasswordEncoder" because it is the best one.
    2. NOTE: don't make multiple beans of the same object as this, because spring will conflict, which one to choose.
    3. if we run the app, spring will conflict.
    4. I do that only for explain concepts.
    */

    // case 1 :
    // this configuration is used by authentication provider by default in login process.
    // but to can same configuration in spring component, you must create a bean of password encoder.
    // without it, if you try to inject passwordEncoder to any class, an error occur.
    @Bean
    public PasswordEncoder passwordEncoder1() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // case 2 :
    // You want to use "DelegatingPasswordEncoder" with your own configuration
    @Bean
    public PasswordEncoder passwordEncoder2() {
        // define a map to hold encoders and associated prefix.
        Map<String, PasswordEncoder> encoderMap = new HashMap<>();

        // Add encoders to map
        encoderMap.put("bcrypt", new BCryptPasswordEncoder());
        encoderMap.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoderMap.put("noop", NoOpPasswordEncoder.getInstance());

        // Return, with make "bcrypt" as the default
        return new DelegatingPasswordEncoder("bcrypt", encoderMap);
    }

    // case 3 :
    // You want to use default "DelegatingPasswordEncoder" but in same time configure bcrypt.
    // Note: You can't modify the map, If you want so use above way.
    @Bean
    public PasswordEncoder passwordEncoder3() {
        DelegatingPasswordEncoder delegatingPasswordEncoder = (DelegatingPasswordEncoder)
                PasswordEncoderFactories.createDelegatingPasswordEncoder();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 20);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(bCryptPasswordEncoder);
        
        return  delegatingPasswordEncoder;
    }

}

