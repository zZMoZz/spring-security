package com.zzz.spring_security.config;

import com.zzz.spring_security.exception.CustomAccessDeniedHandler;
import com.zzz.spring_security.exception.CustomAuthenticationEntryPoint;
import com.zzz.spring_security.filter.emailValidationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/*
 1. we put "/error" in permitted requests, to make error accessible by anyone.
 */


@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // "*" allows all origins, but this is a security risk in the production. replace with a specific domain "http://localhost:4200".
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));  // Specify which origins can access the server.
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));  // Specify which HTTP methods are allowed. (GET, POST, ...)
        corsConfiguration.setAllowCredentials(true);  // Make the server can accept cookies, authorization headers, and others.
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*")); // Set which HTTP  headers can be sent by the client.
        corsConfiguration.setMaxAge(3600L);  // Specify the maximum time that the preflight request response is cached by the browser.

        // Use the implementation class of "corsConfigurationSource"
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // You must specify endpoints that will applied by these configurations, otherwise; no CORS not applied
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}

