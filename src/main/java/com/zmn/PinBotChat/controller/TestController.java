package com.zmn.PinBotChat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from the Spring Server!";
    }

    @GetMapping("/status")
    public String status() {
        return "Server is running and reachable!";
    }
}