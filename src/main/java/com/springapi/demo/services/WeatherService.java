package com.springapi.demo.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    
    @Value("${weather.api.key}")
    private String key;

    @Value("${weather.api.host}")
    private String host;

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    public String getWeather(Double lat, Double lon){
        _LOGGER.info("calling weather API");
        //make api call
        String uri = host + String.format("data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, key, "imperial");
        RestTemplate restTemp = new RestTemplate();
        String res = restTemp.getForObject(uri, String.class);
        _LOGGER.debug("request res: ", res);
        return res;
    }
    
}
