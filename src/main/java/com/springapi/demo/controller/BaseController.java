package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.StatusCode;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class BaseController {
    @GetMapping("/")
    public String getBaseLink() {
        return JsonFormatter.makeJsonResponse(StatusCode.OK, "Welcome to my test application");
    }
}
