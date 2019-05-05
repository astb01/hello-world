package com.hello.hellworld.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping(value = "/{name}")
    public String sayHello(@PathVariable("name") String name) {
        return String.format("Hello %s", name);
    }
}
