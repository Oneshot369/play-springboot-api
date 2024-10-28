package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.model.dataObject.ConstraintModel;
import com.springapi.demo.model.dataObject.LoginAttemptModel;
import com.springapi.demo.model.dataObject.UserLocationModel;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.services.UserService;
import com.springapi.demo.util.ResponseObject;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;



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
    public String getUserById(@RequestParam Long id, Authentication auth){
        User user = (User) auth.getPrincipal();
        return userService.getUserById(id, user);
    }

    @PostMapping("/login")
    public String attemptLogin(@RequestBody LoginAttemptModel loginAttempt){
        return userService.attemptLogin(loginAttempt);
    }

    @GetMapping("/getUserByUsername")
    public String getUserById(@RequestParam String username, Authentication auth){
        User user = (User) auth.getPrincipal();
        return userService.getUserByName(username, user);
    }

    @GetMapping("/getAllUsers")
    public String getAllUsers(Authentication auth){
        User user = (User) auth.getPrincipal();
        return userService.getAllUsers(user);
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserModel user){
        return userService.saveUser(user);
    }

    //--------------------------Locations------------------------

    @PostMapping("/saveLocation")
    public String saveLocation(@RequestBody UserLocationModel userLocation){
        return userService.saveLocationToUser(userLocation, userLocation.getId());
    }
    @PutMapping("/updateLocation")
    public String updateLocation(@RequestBody UserLocationModel locationModel) {
        return userService.updateLocation(locationModel);
    }
    @DeleteMapping("/deleteLocation")
    public String deleteLocation(@RequestParam int id) {
        return userService.deleteLocation(id);
    }
    
    //--------------------------Constraints------------------------

    @PostMapping("/saveConstraint")
    public String saveConstraint(@RequestBody ConstraintModel userConstraint){
        return userService.saveConstraintToUser(userConstraint, userConstraint.getId());
    }
    @PutMapping("/updateConstraint")
    public String updateConstraint(@RequestBody ConstraintModel constraintModel) {
        return userService.updateConstraint(constraintModel);
    }
    @DeleteMapping("/deleteConstraint")
    public String deleteConstraint(@RequestParam int id) {
        return userService.deleteConstraint(id);
    }
}
