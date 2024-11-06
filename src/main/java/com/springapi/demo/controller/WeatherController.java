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

import com.springapi.demo.model.weatherResponse.CurrentWeatherModel;
import com.springapi.demo.model.weatherResponse.ForecastModel;
import com.springapi.demo.model.weatherResponse.submodels.WeatherModel;
import com.springapi.demo.services.WeatherService;
import com.springapi.demo.util.ResponseObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;



// Swagger Annotations - These are only for documentation no function is added in these
@Tag(name = "Weather Controller")
@ApiResponses({
    @ApiResponse(responseCode = "500", description = "Internal Error", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))}),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))}),
    @ApiResponse(responseCode = "404", description = "Not found", content = { @Content(mediaType="application/json", schema = @Schema(implementation = ResponseObject.class))})
})


@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "${cors.frontend.url}")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    @Operation(
        description = "Gets the weather data from the lat and lon of a request",
        summary = "Gets the current weather data"  
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", schema = @Schema(implementation = CurrentWeatherModel.class))})

    @GetMapping("/getWeather")
    public ResponseObject getWeather(@RequestParam Double lat, @RequestParam Double lon){
        _LOGGER.info(String.format("request for weather at location: %s, %s", lat, lon));
        return weatherService.getWeatherFromLatAndLon(lat, lon);
    }

    @Operation(
        description = "Gets location data based on the search term for the name of the location",
        summary = "Gets a list of locations"  
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", array = @ArraySchema(schema = @Schema(implementation = ForecastModel.class)))})

    @GetMapping("/getWeatherFromName")
    public ResponseObject getWeatherFromName(@RequestParam String locationName){
        _LOGGER.info(String.format("request for weather at location: %s", locationName));
        return weatherService.getWeatherFromName(locationName);
    }

    @Operation(
        description = "Gets the forecasted weather bases on the lat an lon of a location",
        summary = "Gets the forecasted weather for a location"  
    )
    @ApiResponse(responseCode = "200",description = "Success", content = { @Content(mediaType="application/json", array = @ArraySchema(schema = @Schema(implementation = ForecastModel.class)))})

    @GetMapping("/getForecast")
    public ResponseObject getForecast(@RequestParam Double lat, @RequestParam Double lon){
        _LOGGER.info(String.format("request for weather at location: %s, %s", lat, lon));
        return weatherService.getForecast(lat, lon);
    }
}
