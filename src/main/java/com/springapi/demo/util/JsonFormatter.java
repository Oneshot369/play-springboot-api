package com.springapi.demo.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.Gson;

public class JsonFormatter {
    
    static Gson js = new Gson();

    public static ResponseObject makeJsonResponse(HttpStatus status, Object value){
        ResponseObject responseObject = new ResponseObject();
        
        responseObject.setData(value);
        responseObject.setStatus(status.value());
        responseObject.setTime(DateUtil.getCurrentTime());
        //ResponseEntity re = new ResponseEntity<>(status);
        return responseObject;
    }

    public static ResponseObject makeJsonResponse(HttpStatusCode status, HttpClientErrorException value){
        ResponseObject responseObject = new ResponseObject();

        responseObject.setData(value.getMessage());
        responseObject.setStatus(status.value());
        responseObject.setTime(DateUtil.getCurrentTime());
        return responseObject;
    }
}
