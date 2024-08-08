package com.springapi.demo.util;

import java.time.LocalDateTime;

import com.google.gson.Gson;

public class JsonFormatter {
    
    static Gson js = new Gson();

    public static String makeJsonResponse(StatusCode status, Object value){
        ResponseObject responseObject = new ResponseObject();

        responseObject.setData(value);
        responseObject.setStatus(status.getStatus());
        responseObject.setTime(LocalDateTime.now().toString());

        return js.toJson(responseObject);
    }
}
