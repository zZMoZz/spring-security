package com.zzz.spring_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello1")
    public String hello1() {
        return "Hello1 Everyone";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "Hello2 Everyone";
    }

    @GetMapping("/hello3")
    public String hello3() {
        return "Hello3 Everyone";
    }

    @GetMapping("/hello4")
    public String hello4() {
        return "Hello4 Everyone";
    }
}
