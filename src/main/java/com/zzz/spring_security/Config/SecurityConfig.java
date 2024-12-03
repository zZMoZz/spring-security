package com.zzz.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
                        .requestMatchers("/hello3", "/hello4", "/error").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
                .username("mohsen")
                .password("{noop}748295")
                .roles("user")
                .build();

        UserDetails user2 = User.builder()
                .username("ahmed")
                .password("{noop}12345")
                .roles("user", "admin")
                .build();

        UserDetails user3 = User.builder()
                .username("amr")
                .password("{noop}54321")
                .roles("user", "admin", "manager")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, user3);
    }
}
