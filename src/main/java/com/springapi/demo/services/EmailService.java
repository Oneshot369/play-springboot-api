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

    private String linkMsg = "\n" + //
            "This email was sent by Weather App for more information on weather conditions please view https://main.dcef6a6ef52te.amplifyapp.com/";

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    /**
     * This sends an email to the recipients email with the desired message.
     * 
     * @param message
     * @param recipient
     * @param subject
     * @return
     */
    public ResponseEntity<ResponseObject> sendEmail(String message, String recipient, String subject) {
        message = message + linkMsg;
        try {
            HttpResponse<JsonNode> request = Unirest.post(host + "v3/" + domain + "/messages")
                    .basicAuth("api", key)
                    .field("from", "Weather Notify <USER@sandbox3753fc66400042709d210daf2731851b.mailgun.org>")
                    .field("to", recipient)
                    .field("subject", subject)
                    .field("text", message)
                    .asJson();
            _LOGGER.info(request.getBody());
        } catch (UnirestException e) {
            return JsonFormatter.makeJsonResponse(HttpStatus.INTERNAL_SERVER_ERROR, "unable to send email");
        }

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, "sent email");
    }

    public void sendEmailsToUsers() {
        List<UserModel> users = userService.getAllUsersForEmail();
        for (UserModel userModel : users) {
            for (UserLocationModel locationModel : userModel.getLocations()) {
                // set to null
                CurrentWeatherModel currentWeather = null;
                // if there are no constraints, then dont waste an api call
                if (!locationModel.getConstraints().isEmpty()) {
                    currentWeather = weatherService.getWeatherFromLatAndLonForEmail(locationModel.getLat(),
                            locationModel.getLon());
                }
                // ignore if null
                if (currentWeather != null) {
                    for (ConstraintModel constraintModel : locationModel.getConstraints()) {
                        switch (constraintModel.getCondition()) {
                            case TEMP:
                                getTemp(userModel, locationModel, currentWeather, constraintModel);
                                break;
                            case FEELS_LIKE:
                                getFeelsLike(userModel, locationModel, currentWeather, constraintModel);
                                break;
                            case PRESSURE:
                                getPressure(userModel, locationModel, currentWeather, constraintModel);
                                break;
                            case HUMIDITY:
                                getHumidity(userModel, locationModel, currentWeather, constraintModel);
                                break;
                            case VISIBILITY:
                                getVisibility(userModel, locationModel, currentWeather, constraintModel);
                                break;
                            case WIND_SPEED:
                                getWindSpeed(userModel, locationModel, currentWeather, constraintModel);
                                break;
                            case WIND_GUST:
                                getGust(userModel, locationModel, currentWeather, constraintModel);
                                break;
                            default:

                        }
                    }
                }
            }
        }
    }

    /**
     * checks if a certain constraint is above / below the temp of the location.
     * 
     * @param userModel
     * @param locationModel
     * @param currentWeather
     * @param constraintModel
     */
    private void getTemp(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,
            ConstraintModel constraintModel) {
        double val = currentWeather.getMain().getTemp();
        String sub = String.format("The %s alert was triggered", constraintModel.getName());
        String head = String.format("Your constraint %s was activated. \n", constraintModel.getName());
        try {
            if (constraintModel.isGreaterThan()) {
                if (val > Double.parseDouble(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The temperature in %s is currently %.2f degrees. This is above the alert threshold of %s degrees.",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            } else {
                if (val < Double.parseDouble(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The temperature in %s is currently %.2f degrees. This is below the alert threshold of %s degrees",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            }
        } catch (Exception e) {
            _LOGGER.error(String.format("Error in Email for user: %d, location: %d, constraint: %d", userModel.getId(),
                    locationModel.getId(), constraintModel.getId()));
            _LOGGER.error(e.getMessage());
        }
    }

    // I still need to finish up these
    private void getFeelsLike(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,
            ConstraintModel constraintModel) {
        double val = currentWeather.getMain().getFeels_like();
        String sub = String.format("The %s alert was triggered", constraintModel.getName());
        String head = String.format("Your constraint %s was activated. \n", constraintModel.getName());
        try {
            if (constraintModel.isGreaterThan()) {
                if (val > Double.parseDouble(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "In %s it currently feels like %.2f degrees. This is above the alert threshold of %s degrees",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            } else {
                if (val < Double.parseDouble(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "In %s it currently feels like %.2f degrees. This is below the alert threshold of %s degrees",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            }
        } catch (Exception e) {
            _LOGGER.error(String.format("Error in Email for user: %d, location: %d, constraint: %d", userModel.getId(),
                    locationModel.getId(), constraintModel.getId()));
            _LOGGER.error(e.getMessage());
        }
    }

    private void getPressure(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,
            ConstraintModel constraintModel) {
        int val = currentWeather.getMain().getPressure();
        String sub = String.format("The %s alert was triggered", constraintModel.getName());
        String head = String.format("Your constraint %s was activated. \n", constraintModel.getName());
        try {
            if (constraintModel.isGreaterThan()) {
                if (val > Integer.parseInt(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The pressure in %s is currently %d hPa. This is above the alert threshold of %s hPa",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            } else {
                if (val < Integer.parseInt(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The pressure in %s is currently %d hPa. This is below the alert threshold of %s hPa",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            }
        } catch (Exception e) {
            _LOGGER.error(String.format("Error in Email for user: %d, location: %d, constraint: %d", userModel.getId(),
                    locationModel.getId(), constraintModel.getId()));
            _LOGGER.error(e.getMessage());
        }
    }

    private void getHumidity(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,
            ConstraintModel constraintModel) {
        int val = currentWeather.getMain().getHumidity();
        String sub = String.format("The %s alert was triggered", constraintModel.getName());
        String head = String.format("Your constraint %s was activated. \n", constraintModel.getName());
        try {
            if (constraintModel.isGreaterThan()) {
                if (val > Integer.parseInt(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The humidity in %s is currently %d %. This is above the alert threshold of %s %",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            } else {
                if (val < Integer.parseInt(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The humidity in %s is currently %d degrees. This is below the alert threshold of %s%",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            }
        } catch (Exception e) {
            _LOGGER.error(String.format("Error in Email for user: %d, location: %d, constraint: %d", userModel.getId(),
                    locationModel.getId(), constraintModel.getId()));
            _LOGGER.error(e.getMessage());
        }
    }

    private void getVisibility(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,
            ConstraintModel constraintModel) {
        int val = currentWeather.getVisibility();
        String sub = String.format("The %s alert was triggered", constraintModel.getName());
        String head = String.format("Your constraint %s was activated. \n", constraintModel.getName());
        try {
            if (constraintModel.isGreaterThan()) {
                if (val > Integer.parseInt(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The visibility in %s is currently %d miles This is above the alert threshold of %s miles",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            } else {
                if (val < Integer.parseInt(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The visibility in %s is currently %d miles. This is below the alert threshold of %s miles",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            }
        } catch (Exception e) {
            _LOGGER.error(String.format("Error in Email for user: %d, location: %d, constraint: %d", userModel.getId(),
                    locationModel.getId(), constraintModel.getId()));
            _LOGGER.error(e.getMessage());
        }
    }

    private void getWindSpeed(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,
            ConstraintModel constraintModel) {
        double val = currentWeather.getWind().getSpeed();
        String sub = String.format("The %s alert was triggered", constraintModel.getName());
        String head = String.format("Your constraint %s was activated. \n", constraintModel.getName());
        try {
            if (constraintModel.isGreaterThan()) {
                if (val > Double.parseDouble(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The wind speed in %s is currently %.2f mph. This is above the alert threshold of %s mph",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            } else {
                if (val < Double.parseDouble(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The wind speed in %s is currently %.2f mph. This is below the alert threshold of %s mph",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            }
        } catch (Exception e) {
            _LOGGER.error(String.format("Error in Email for user: %d, location: %d, constraint: %d", userModel.getId(),
                    locationModel.getId(), constraintModel.getId()));
            _LOGGER.error(e.getMessage());
        }
    }

    private void getGust(UserModel userModel, UserLocationModel locationModel, CurrentWeatherModel currentWeather,
            ConstraintModel constraintModel) {
        double val = currentWeather.getWind().getGust();
        String sub = String.format("The %s alert was triggered", constraintModel.getName());
        String head = String.format("Your constraint %s was activated. \n", constraintModel.getName());
        try {
            if (constraintModel.isGreaterThan()) {
                if (val > Double.parseDouble(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The wind gust in %s is currently %.2f mph. This is above the alert threshold of %s mph",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            } else {
                if (val < Double.parseDouble(constraintModel.getVal())) {
                    // insert all of the constraint information
                    String message = String.format(
                            "The wind gust in %s is currently %.2f mph. This is below the alert threshold of %s mph",
                            locationModel.getName(), val, constraintModel.getVal());
                    // insert a header for the message
                    message = head + message;
                    sendEmail(message, userModel.getUsername(), sub);
                }
            }
        } catch (Exception e) {
            _LOGGER.error(String.format("Error in Email for user: %d, location: %d, constraint: %d", userModel.getId(),
                    locationModel.getId(), constraintModel.getId()));
            _LOGGER.error(e.getMessage());
        }
    }

}
