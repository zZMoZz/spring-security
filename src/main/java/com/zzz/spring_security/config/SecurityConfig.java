package com.zzz.spring_security.config;

import com.zzz.spring_security.exception.CustomAccessDeniedHandler;
import com.zzz.spring_security.exception.CustomAuthenticationEntryPoint;
import com.zzz.spring_security.filter.emailValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/*
 1. we put "/error" in permitted requests, to make error accessible by anyone.
 */


@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(new emailValidationFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/hello1", "/hello2").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/hello3").hasRole("ADMIN")
                        .requestMatchers("/hello4", "/error", "/register").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(hbc -> hbc
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ehc -> ehc
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .build();
    }

    // this configuration is used by authentication provider by default in login process.
    // but to can use same configuration in spring components, you must create a bean
    // of password encoder. without that, if you try to inject passwordEncoder to
    // any components, an error occur.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}


