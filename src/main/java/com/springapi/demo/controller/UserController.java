package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.model.dataObject.ConstraintModel;
import com.springapi.demo.model.dataObject.LoginAttemptModel;
import com.springapi.demo.model.dataObject.UserLocationModel;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.services.UserService;
import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.ResponseObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

// Swagger Annotations - These are only for documentation no function is added in these
@Tag(name = "User endpoints")
@ApiResponses({
    @ApiResponse(responseCode = "500", description = "Internal Error", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))}),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))}),
    @ApiResponse(responseCode = "404", description = "Not found", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))})
})
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "BasicAuth", scheme = "bearer", bearerFormat= "JWT")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
        description = "Returns a userModel by its ID by querying the DB, will require an admin account.",
        summary = "Gets a user by its ID"
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(implementation = UserModel.class))})
    @SecurityRequirement(name="BasicAuth")

    @GetMapping("/getUserById")
    public ResponseEntity<ResponseObject> getUserById(
        @RequestParam Long id, 
        Authentication auth){

        User user = (User) auth.getPrincipal();
        return userService.getUserById(id, user);
    }

    @Operation(
        description = "Returns a token of the JTW if it is successful",
        summary = "Attempts a Login" 
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "\"JWT token\""))})

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> attemptLogin(@RequestBody LoginAttemptModel loginAttempt){
        return userService.attemptLogin(loginAttempt);
    }

    @Operation(
        description = "This is meant as a testing only endpoint, wont be in prod",
        summary = "Gets a user by their username - TESTING ENDPOINT" 
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(implementation = UserModel.class))})

    @GetMapping("/getUserByUsername")
    public ResponseEntity<ResponseObject> getUserById(@RequestParam String username, Authentication auth){
        User user = (User) auth.getPrincipal();
        return userService.getUserByName(username, user);
    }

    @Operation(
        description = "This will return every user in our database, will require an admin account.",
        summary = "Returns every User" 
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", array = @ArraySchema( schema = @Schema(implementation = UserModel.class)))})
    @SecurityRequirement(name="BasicAuth")

    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseObject> getAllUsers(Authentication auth){
        User user = (User) auth.getPrincipal();
        return userService.getAllUsers(user);
    }

    @Operation(
        description = "This saves a user to our database, the Locations and constraints now not need to be filled out and can be left blank",
        summary = "Saves a user to the DB" 
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})

    @PostMapping("/saveUser")
    public ResponseEntity<ResponseObject> saveUser(@RequestBody UserModel user){
        return userService.saveUser(user);
    }
    @Operation(
        description = "You need to be an admin to have this feature, and it deletes all locations and constraints with he account",
        summary = "Deletes a users account"
    )

    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})
    @SecurityRequirement(name="BasicAuth")

    @DeleteMapping("/deleteUser")
    public ResponseEntity<ResponseObject> deleteUser(@RequestParam int id) {
        return userService.deleteUser(Long.valueOf(id));
    }


    //--------------------------Locations------------------------
    @Tag(name = "Locations")
    @Operation(
        description = "The user needs have the JWT token in the request or this will fail as the token is how we identify what account to save the location to. The constraints will be ignored, this only for updating a location.",
        summary = "Saves a location to a users account" 
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})
    @SecurityRequirement(name="BasicAuth")

    @PostMapping("/saveLocation")
    public ResponseEntity<ResponseObject> saveLocation(Authentication auth, @RequestBody UserLocationModel userLocation){
        User user = (User) auth.getPrincipal();
        return userService.saveLocationToUser(userLocation, user);
    }

    @Tag(name = "Locations")
    @Operation(
        description = "The user needs have the JWT token in the request or this will fail as the token is how we identify what account to update the location to. The constraints will be ignored, this only for updating a location.",
        summary = "Updates a location from a users account"
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})
    @SecurityRequirement(name="BasicAuth")

    @PutMapping("/updateLocation")
    public ResponseEntity<ResponseObject> updateLocation(@RequestBody UserLocationModel locationModel) {
        return userService.updateLocation(locationModel);
    }

    @Tag(name = "Locations")
    @Operation(
        description = "The user needs have the JWT token in the request or this will fail as the token is how we identify what account to delete the location from. Deleting the location will delete any constraints tied to it.",
        summary = "Deletes a location from a users account"
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})
    @SecurityRequirement(name="BasicAuth")

    @DeleteMapping("/deleteLocation")
    public ResponseEntity<ResponseObject> deleteLocation(@RequestParam int id) {
        return userService.deleteLocation(id);
    }
    @Tag(name = "Locations")
    @Operation(
        description = "The user needs have the JWT token in the request or this will fail as the token is how we identify what account we are getting the locations for.",
        summary = "Gets all locations from a users account" 
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})
    @SecurityRequirement(name="BasicAuth")

    @GetMapping("/getLocation")
    public ResponseEntity<ResponseObject> getLocations(Authentication auth){
        User user = (User) auth.getPrincipal();
        return userService.getAllLocationsForUser(user);
    }
    
    //--------------------------Constraints------------------------

    @Tag(name = "Constraints")
    @Operation(
        description = "The user needs have the JWT token in the request or this will fail as the token is how we identify what account to save the constraint to",
        summary = "Saves a constraint to a users location" 
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})
    @SecurityRequirement(name="BasicAuth")

    @PostMapping("/saveConstraint")
    public ResponseEntity<ResponseObject> saveConstraint(Authentication auth, @RequestBody ConstraintModel userConstraint){
        return userService.saveConstraintToUser(userConstraint, userConstraint.getId());
    }

    @Tag(name = "Constraints")
    @Operation(
        description = "The user needs have the JWT token in the request or this will fail as the token is how we identify what account to update the constraint to",
        summary = "Updates a constraint to a users location" 
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})
    @SecurityRequirement(name="BasicAuth")

    @PutMapping("/updateConstraint")
    public ResponseEntity<ResponseObject> updateConstraint(@RequestBody ConstraintModel constraintModel) {
        return userService.updateConstraint(constraintModel);
    }

    @Tag(name = "Constraints")
    @Operation(
        description = "The user needs have the JWT token in the request or this will fail as the token is how we identify what account to delete the constraint from",
        summary = "Deletes a constraint to a users location"  
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(type = "string", example = "Success"))})
    @SecurityRequirement(name="BasicAuth")

    @DeleteMapping("/deleteConstraint")
    public ResponseEntity<ResponseObject> deleteConstraint(@RequestParam int id) {
        return userService.deleteConstraint(id);
    }
}
