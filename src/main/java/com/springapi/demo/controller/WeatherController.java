package com.springapi.demo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.springapi.demo.model.weatherResponse.CurrentWeatherModel;
import com.springapi.demo.model.weatherResponse.ForecastModel;
import com.springapi.demo.model.weatherResponse.LocationModel;
import com.springapi.demo.services.WeatherService;
import com.springapi.demo.util.JsonFormatter;
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
@CrossOrigin(origins = "${cors.frontend.url}")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    @GetMapping("/getWeather")
    public String getWeather(@RequestParam Double lat, @RequestParam Double lon){
        _LOGGER.info(String.format("request for weather at location: %s, %s", lat, lon));
        CurrentWeatherModel currentWeatherModel;
        try{
            currentWeatherModel = weatherService.getWeatherFromLatAndLon(lat, lon);
        }catch(HttpClientErrorException errorException){
            return JsonFormatter.makeJsonResponse(errorException.getStatusCode(), errorException);
        }
        
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, currentWeatherModel);
    }
    @GetMapping("/getForecast")
    public String getForecast(@RequestParam Double lat, @RequestParam Double lon){
        _LOGGER.info(String.format("request for forecast at location: %s, %s", lat, lon));
        return weatherService.getForecastFromLatAndLon(lat, lon);
    }
    @GetMapping("/getWeatherFromName")
    public String getWeatherFromName(@RequestParam String locationName){
        _LOGGER.info(String.format("request for weather at location: %s", locationName));
        List<LocationModel> locationModel;
        try{
            locationModel = weatherService.getWeatherFromName(locationName);
        }catch(HttpClientErrorException errorException){
            return JsonFormatter.makeJsonResponse(errorException.getStatusCode(), errorException);
        }
        if(locationModel.isEmpty()){
            return JsonFormatter.makeJsonResponse(HttpStatus.NOT_FOUND, "No locations Found");
        }

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, locationModel);
    }
    @GetMapping("/getForecast")
    public String getForecast(@RequestParam Double lat, @RequestParam Double lon){
        _LOGGER.info(String.format("request for weather at location: %s, %s", lat, lon));
        ForecastModel forecastModel;
        try{
            forecastModel = weatherService.getForecast(lat, lon);
        }catch(HttpClientErrorException errorException){
            return JsonFormatter.makeJsonResponse(errorException.getStatusCode(), errorException);
        }
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, forecastModel);
    }
}
