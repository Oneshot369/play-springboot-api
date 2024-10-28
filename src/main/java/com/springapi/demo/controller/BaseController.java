package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.ResponseObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api/v1")

// Swagger Annotations - These are only for documentation
@Tag(name = "Default Controller")
@ApiResponses({
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))}),
    @ApiResponse(responseCode = "500", description = "Internal Error", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))})
})
public class BaseController {

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    @Autowired
	private Environment env;


    @GetMapping("/")
    @Operation(
        description = "Health check endpoint that responds with static data",
        summary = "Health check endpoint"
    )
    public String getBaseLink() {
        String[] activeProfiles = env.getActiveProfiles();
        if(activeProfiles !=null)
            _LOGGER.info("in env: " + activeProfiles[0]);
        
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, "Welcome to my test application");
    }

    
    @GetMapping("/auth")
    public String testAuth(Authentication auth) {      
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, "you must be logged in to see this");
    }
    
}
