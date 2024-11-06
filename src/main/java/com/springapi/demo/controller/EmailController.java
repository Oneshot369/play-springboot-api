package com.springapi.demo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.services.EmailService;
import com.springapi.demo.util.ResponseObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/email")

// Swagger Annotations - These are only for documentation no function is added in these
@Tag(name = "Email Endpoints (TESTING PURPOSES: these endpoints are for testing the email, this will not be in the final product)")
@ApiResponses({
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))}),
    @ApiResponse(responseCode = "500", description = "Internal Error", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))})
})
public class EmailController {

    @Autowired
    private EmailService emailService;

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    @GetMapping("/sendEmail")
    @Operation(
        description = "Takes the message and sends an email to your account with that message",
        summary = "Sends a email - FOR TESTING ONLY, WILL BE REMOVED IN PRODUCTION"
    )
    public ResponseObject getWeatherTest(@RequestBody String message){
        _LOGGER.info(String.format("Request to send email with message %s", message));
        return emailService.sendEmail(message);
    }
}
