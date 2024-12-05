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
    1. We use "DelegatingPasswordEncoder" in all examples because it is the best approach.
    2. NOTE: don't create multiple beans of the same object as we do here, because spring will get conflict, which one to choose!
    -------> if we run the app, spring will conflict.
    -------> I do that only for write all code cases without comment them.
    */

    // Example 1 :
    // this creates "DelegatingPasswordEncoder" supports a lot of password encoders.
    // ------> with "bcrypt" as for encoding process.
    // this configuration is used by authentication provider by default in login process.
    // but to can use "PasswordEncoder" spring components, you must create a bean of it.
    // without this, if you try to inject passwordEncoder to any spring component, an error occur.
    @Bean
    public PasswordEncoder passwordEncoder1() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Example 2 :
    // We were used "pbkdf2" and want to upgrade to "bcrypt". => so use "bcrypt" for encoding.
    // Notice, there some users their password still stored as plain text and without prefixies.
    //   --> So, in this case we must declare a default encoder that handle passwords without prefixies.
    @Bean
    public PasswordEncoder passwordEncoder2() {
         // define a map to hold encoders and associated prefix.
        Map<String, PasswordEncoder> encoders = new HashMap<>();

         // Add encoders to map
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());

        // Create instance of DelegatingPasswordEncoder to can edit before return
        DelegatingPasswordEncoder delegatingPwdEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);

        // specify a default encoder to handle that without prefix
        delegatingPwdEncoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());

        // Return, with use "bcrpyt" for encoding operation
        return delegatingPwdEncoder;
    }
 
    // Example 3 :
     // You want to use "DelegatingPasswordEncoder" with your own configuration.

    // If you want to tune "BCryptPasswordEncoder", you can't use "createDelegatingPasswordEncoder()".
    // --> Because you can't modify map list of encoders or even override it.
    // So, it is better to create "DelegatingPasswordEncoder" with your own configuration.
         /* 
        ==================================================================================
        !!!!! NOTE: these properties are applied to new passwords during registration. it doesn't mean thing 
        for matching process, because salt, strength, and version already extracts from stored hashed password.
        So don't warry it will not affect the passwords that were stored using old configuration.
        ==================================================================================
        */
    @Bean
    public PasswordEncoder passwordEncoder2() {
         // define a map to hold encoders and associated prefix.
        Map<String, PasswordEncoder> encodersMap = new HashMap<>();

         // Add encoders to the map
        encodersMap.put("bcrypt", new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 20));
        // add what you want .... 

        // Return, with use "bcrpyt" for encoding operation
        return new DelegatingPasswordEncoder("bcrypt", encodersMap);
    }
}


