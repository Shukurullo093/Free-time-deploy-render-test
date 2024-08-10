package com.time.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("")
    public String test() {
        return "<h1>Project is working...</h1>";
    }
}
