package com.springapi.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public class DateUtil {

    @Value("${date.format}")
    private static String format;


    public static String getCurrentTime(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }
}
