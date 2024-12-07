package com.springapi.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.springapi.demo.model.weatherResponse.CurrentWeatherModel;
import com.springapi.demo.model.weatherResponse.ForecastModel;
import com.springapi.demo.model.weatherResponse.LocationModel;
import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.ResponseObject;

@Service
@CrossOrigin("${cors.frontend.url}")
public class WeatherService {
    
    @Value("${weather.api.key}")
    private String key;

    @Value("${weather.api.host}")
    private String host;

    RestTemplate restTemp = new RestTemplate();

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    public ResponseEntity<ResponseObject> getWeatherFromLatAndLon(Double lat, Double lon) throws HttpClientErrorException{
        _LOGGER.info(String.format("Getting weather from lat: %f, lon: %f", lat, lon));
        CurrentWeatherModel res;
        //make api call
        String uri = host + String.format("data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, key, "imperial");
        //RestTemplate restTemp = new RestTemplate();
        try{
            res = restTemp.getForObject(uri, CurrentWeatherModel.class);
        }
        catch(HttpClientErrorException errorException){
            _LOGGER.warn("Error trying to get Weather", errorException);
            return JsonFormatter.makeJsonResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorException.getMessage());
        }
       
        _LOGGER.debug("request res: ", res);
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, res);
    }
    public ResponseEntity<ResponseObject> getWeatherFromName(String locationName){
        _LOGGER.info("calling weather API2");
        //make api call
        String uri = host + String.format("geo/1.0/direct?q=%s&limit=5&appid=%s", locationName, key, "imperial");
        //RestTemplate restTemp = new RestTemplate();
        List<LocationModel> res = new ArrayList<>();
        try{
            LocationModel[] arrayRes = restTemp.getForObject(uri, LocationModel[].class);
            res = List.of(arrayRes);
        }
        catch(HttpClientErrorException errorException){
            _LOGGER.warn("Error trying to get name of location", errorException);
            return JsonFormatter.makeJsonResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorException.getMessage());
        }
        _LOGGER.debug("request res: ", res);
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, res);
    }

    public ResponseEntity<ResponseObject> getForecast(Double lat, Double lon) {
        _LOGGER.info(String.format("Getting Forecast from lat: %f, lon: %f", lat, lon));
        //make api call
        String uri = host + String.format("data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=%s&cnt=%d", lat, lon, key, "imperial", 10);
        //RestTemplate restTemp = new RestTemplate();
        
        ForecastModel res;
        try{
            res = restTemp.getForObject(uri, ForecastModel.class);
        }
        catch(HttpClientErrorException errorException){
            _LOGGER.warn("Error trying to get Weather", errorException);
            return JsonFormatter.makeJsonResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorException.getMessage());
        }
       
        _LOGGER.debug("request res: ", res);
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, res);
    }
    // public String getForecastFromLatAndLon(Double lat, Double lon) {
    //     _LOGGER.info("calling weather API: get forecast weather");
    //     //make api call
    //     String uri = host + String.format("data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, key, "imperial");
    //     RestTemplate restTemp = new RestTemplate();
    //     String res = restTemp.getForObject(uri, String.class);
    //     _LOGGER.debug("request res: ", res);
    //     return res;
    // }
    
}
