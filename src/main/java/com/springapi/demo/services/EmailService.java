package com.springapi.demo.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mashape.unirest.http.HttpResponse;
  import com.mashape.unirest.http.JsonNode;
  import com.mashape.unirest.http.Unirest;
  import com.mashape.unirest.http.exceptions.UnirestException;
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

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    public ResponseObject sendEmail(String message){
        try{
            HttpResponse<JsonNode> request = Unirest.post(host + domain + "/messages")
            .basicAuth("api", key)
            .field("from", "Weather App <USER@sandbox3753fc66400042709d210daf2731851b.mailgun.org>")
            .field("to", "joshother2003@gmail.com")
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
    
}
