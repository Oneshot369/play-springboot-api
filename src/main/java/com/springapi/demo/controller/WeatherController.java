package com.springapi.demo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springapi.demo.services.WeatherService;
import com.springapi.demo.util.ResponseObject;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/weather")

// Swagger Annotations - These are only for documentation
@Tag(name = "Default Controller")
@ApiResponses({
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))}),
    @ApiResponse(responseCode = "500", description = "Internal Error", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))})
})
@CrossOrigin(origins = "http://localhost:5173")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    @GetMapping("/getWeather")
    public String getWeather(@RequestParam Double lat, @RequestParam Double lon){
        _LOGGER.info(String.format("request for weather at location: %s, %s", lat, lon));
        return weatherService.getWeatherFromLatAndLon(lat, lon);
    }
    @GetMapping("/getWeatherFromName")
    public String getWeatherFromName(@RequestParam String locationName){
        _LOGGER.info(String.format("request for weather at location: %s", locationName));
        return weatherService.getWeatherFromName(locationName);
    }
}
