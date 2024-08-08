package com.springapi.demo.services;

import java.sql.Time;
import java.time.LocalDateTime;

import org.json.JSONObject;

public class JsonFormatter {
    
    JSONObject js;

    public JsonFormatter(int status){
        js = new JSONObject();
        js.put("response", status);
    }

    public void put(String key, Object value){
        js.put(key, value);
    }
    @Override
    public String toString() {
        //add the time
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + "T" + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond(); 
        js.put("timestamp", currentTime);

        return js.toString();
    }
}
