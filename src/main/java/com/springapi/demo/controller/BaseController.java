package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.StatusCode;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class BaseController {
    @GetMapping("/")
    public String getBaseLink() {
        return JsonFormatter.makeJsonResponse(StatusCode.OK, "Welcome to my test application");
    }
    @GetMapping("/codes")
    public String getcodes() {
        //make a hash map for the codes
        HashMap<String, Integer> resCodesMap = new HashMap<>();
        for (StatusCode val : StatusCode.values()) {
            resCodesMap.put(val.name(), val.getStatus());
        }
        return JsonFormatter.makeJsonResponse(StatusCode.OK, resCodesMap);
    }
}
