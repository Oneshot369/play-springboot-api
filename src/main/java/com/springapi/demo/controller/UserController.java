package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserById")
    public String getUserById(@RequestParam Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/getAllUsers")
    public String getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserModel id){
        return userService.saveUser(id);
    }
}
