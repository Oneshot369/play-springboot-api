package com.springapi.demo.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.api.model.UserModel;
import com.springapi.demo.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getUserById")
    public UserModel getUserById(@RequestParam Integer id){
        return userService.getUserById(id);
    }
}
