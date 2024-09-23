package com.springapi.demo.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
  import com.mashape.unirest.http.HttpResponse;
  import com.mashape.unirest.http.JsonNode;
  import com.mashape.unirest.http.Unirest;
  import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class EmailService {
    
    @Value("${email.api.key}")
    private String key;

    @Value("${email.api.host}")
    private String host;

    @Value("${email.api.domain}")
    private String domain;

    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    public String sendEmail(String message){
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
            return "unable to send email";
        }
        
        return "Email Sent";
    }
    
}
