package com.springapi.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static String format = "yyyy-MM-dd HH:mm:ss";


    public static String getCurrentTime(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }
}
