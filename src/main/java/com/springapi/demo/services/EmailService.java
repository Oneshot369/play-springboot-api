package com.springapi.demo.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.springapi.demo.model.dataObject.ConstraintModel;
import com.springapi.demo.model.dataObject.UserLocationModel;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.model.weatherResponse.CurrentWeatherModel;
import com.springapi.demo.model.weatherResponse.LocationModel;
import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.ResponseObject;


@Service
public class EmailService {
    
    @Value("${email.api.key}")
    private String key;

    @Value("${email.api.host}")
    private String host;

    @Value("${email.api.domain}")
    private String domain;

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    public ResponseEntity<ResponseObject> sendEmail(String message, String recipient){
        List<UserModel> users = userService.getAllUsersForEmail();
        
        try{
            HttpResponse<JsonNode> request = Unirest.post(host+"v3/" + domain + "/messages")
            .basicAuth("api", key)
            .field("from", "Weather App <USER@sandbox3753fc66400042709d210daf2731851b.mailgun.org>")
            .field("to", recipient)
            .field("subject", "hello")
            .field("text", message)
            .asJson();
            _LOGGER.info(request.getBody());
        }
        catch(UnirestException e){
            return JsonFormatter.makeJsonResponse(HttpStatus.INTERNAL_SERVER_ERROR, "unable to send email");
        }
        
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, "sent email");
    }

    public void addEmailToWhiteList(String emailAddress){
        try{
            HttpResponse<JsonNode> request = Unirest.post(host + "v2/ip_whitelist")
            .basicAuth("api", key)
            .body(emailAddress)
            .asJson();
            _LOGGER.info(request.getBody());
        }
        catch(UnirestException e){
            _LOGGER.error(e.getMessage());
        }
    }

    public void sendEmailsToUsers(){
        List<UserModel> users = userService.getAllUsersForEmail();
        for (UserModel userModel : users) {
            for (UserLocationModel locationModel : userModel.getLocations()) {
                //set to null
                CurrentWeatherModel currentWeather = null;
                //if there are no constraints, then dont waste an api call
                if(!locationModel.getConstraints().isEmpty()){
                    currentWeather = weatherService.getWeatherFromLatAndLonForEmail(locationModel.getLat(), locationModel.getLon());
                }
                //ignore if null
                if(currentWeather != null){
                    for (ConstraintModel constraintModel : locationModel.getConstraints()) {
                        switch(constraintModel.getCondition()){
                            case TEMP:
                                getTemp(userModel, locationModel, currentWeather, constraintModel);

                            case FEELS_LIKE:

                            case PRESSURE:
                            case HUMIDITY:
                            case VISIBILITY:
                            case WIND_DEG:
                            case WIND_GUST:
                            default:
                        }
                    }
                } 
            }
        }
    }

    private void getTemp(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,ConstraintModel constraintModel) {
        double temp = currentWeather.getMain().getTemp();
        try{
            if(constraintModel.isGreaterThan()){
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %.2f degrees. This is above the alert threshold of %s degrees",locationModel.getName(), temp, constraintModel.getVal());
                    sendEmail(message, userModel.getUsername());
                }
            }else{
                if(temp < Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %.2f degrees. This is below the alert threshold of %s degrees",locationModel.getName(), temp, constraintModel.getVal());
                    sendEmail(message, userModel.getUsername());
                }
            }
        }catch(Exception e){
            _LOGGER.error(String.format("Error in Email for user:%f, location:%f, constraint:%f",userModel.getId(), locationModel.getId(), constraintModel.getId()));
            _LOGGER.error(e.getMessage());
        }
    }
    // I still need to finish up these
    private void getFeelsLike(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,ConstraintModel constraintModel) {
        double temp = currentWeather.getMain().getTemp();
        try{
            if(constraintModel.isGreaterThan()){
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is above the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }else{
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %.2d degrees. This is below the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }
        }catch(Exception e){
            _LOGGER.error(String.format("Error in Email for user:%d, location:%d, constraint:%d",userModel.getId(), locationModel.getId(), constraintModel.getId()));
        }
    }
    private void getPressure(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,ConstraintModel constraintModel) {
        double temp = currentWeather.getMain().getTemp();
        try{
            if(constraintModel.isGreaterThan()){
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is above the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }else{
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is below the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }
        }catch(Exception e){
            _LOGGER.error(String.format("Error in Email for user:%d, location:%d, constraint:%d",userModel.getId(), locationModel.getId(), constraintModel.getId()));
        }
    }
    private void getHumidity(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,ConstraintModel constraintModel) {
        double temp = currentWeather.getMain().getTemp();
        try{
            if(constraintModel.isGreaterThan()){
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is above the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }else{
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is below the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }
        }catch(Exception e){
            _LOGGER.error(String.format("Error in Email for user:%d, location:%d, constraint:%d",userModel.getId(), locationModel.getId(), constraintModel.getId()));
        }
    }
    private void getVisibility(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,ConstraintModel constraintModel) {
        double temp = currentWeather.getMain().getTemp();
        try{
            if(constraintModel.isGreaterThan()){
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is above the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }else{
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is below the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }
        }catch(Exception e){
            _LOGGER.error(String.format("Error in Email for user:%d, location:%d, constraint:%d",userModel.getId(), locationModel.getId(), constraintModel.getId()));
        }
    }
    private void getWindDegree(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,ConstraintModel constraintModel) {
        double temp = currentWeather.getMain().getTemp();
        try{
            if(constraintModel.isGreaterThan()){
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is above the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }else{
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is below the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }
        }catch(Exception e){
            _LOGGER.error(String.format("Error in Email for user:%d, location:%d, constraint:%d",userModel.getId(), locationModel.getId(), constraintModel.getId()));
        }
    }
    private void getGust(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,ConstraintModel constraintModel) {
        double temp = currentWeather.getMain().getTemp();
        try{
            if(constraintModel.isGreaterThan()){
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is above the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }else{
                if(temp > Double.parseDouble(constraintModel.getVal())){
                    String message = String.format("The temperature in %s is currently %d degrees. This is below the alert threshold of %d degrees",locationModel.getName(), currentWeather.getMain().getTemp(), temp );
                    sendEmail(message, userModel.getUsername());
                }
            }
        }catch(Exception e){
            _LOGGER.error(String.format("Error in Email for user:%d, location:%d, constraint:%d",userModel.getId(), locationModel.getId(), constraintModel.getId()));
        }
    }
    
}
