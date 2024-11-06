package com.springapi.demo.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.Gson;

public class JsonFormatter {
    
    static Gson js = new Gson();

    public static ResponseEntity<ResponseObject> makeJsonResponse(HttpStatus status, Object value){
        ResponseObject responseObject = new ResponseObject();
        
        responseObject.setData(value);
        responseObject.setTime(DateUtil.getCurrentTime());
        ResponseEntity<ResponseObject> re = new ResponseEntity<>(responseObject , status);
        
        return re;
    }

    public static ResponseEntity<?> makeJsonResponse(HttpStatusCode status, HttpClientErrorException value){
        ResponseObject responseObject = new ResponseObject();
        
        responseObject.setData(value);
        responseObject.setTime(DateUtil.getCurrentTime());
        ResponseEntity<ResponseObject> re = new ResponseEntity<>(responseObject , status);
        
        return re;
    }
}
