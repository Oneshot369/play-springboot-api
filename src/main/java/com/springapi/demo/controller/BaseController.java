package com.springapi.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.ResponseObject;
import com.springapi.demo.util.StatusCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
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
        
        return JsonFormatter.makeJsonResponse(StatusCode.OK, "Welcome to my test application");
    }
    
    @Operation(summary = "Gets Possible response codes",
            description = "This endpoint gets all of our response codes")
    @GetMapping("/codes")
    public String getCodes() {
        //make a hash map for the codes
        HashMap<String, Integer> resCodesMap = new HashMap<>();
        for (StatusCode val : StatusCode.values()) {
            resCodesMap.put(val.name(), val.getStatus());
        }
        return JsonFormatter.makeJsonResponse(StatusCode.OK, resCodesMap);
    }
}
