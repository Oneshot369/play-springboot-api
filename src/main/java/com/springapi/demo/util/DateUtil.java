package com.springapi.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static String format = "yyyy-mm-dd hh:mm:ss";


    public static String getCurrentTime(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }
}
