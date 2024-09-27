package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.services.UserService;
import com.springapi.demo.util.ResponseObject;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/v1/user")

// Swagger Annotations - These are only for documentation
@Tag(name = "User endpoints")
@ApiResponses({
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))}),
    @ApiResponse(responseCode = "500", description = "Internal Error", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))})
})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserById")
    // @Hidden -  this annotation can be used to hide an endpoint
    public String getUserById(@RequestParam Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/getAllUsers")
    public String getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserModel user){
        return userService.saveUser(user);
    }
}
