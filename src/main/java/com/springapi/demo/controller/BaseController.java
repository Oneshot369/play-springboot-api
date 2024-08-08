package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.services.JsonFormatter;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class BaseController {
    @GetMapping("/")
    public String getBaseLink() {
        JsonFormatter json = new JsonFormatter(200);
        json.put("data", "Welcome to my test application");

        return json.toString();
    }
}
