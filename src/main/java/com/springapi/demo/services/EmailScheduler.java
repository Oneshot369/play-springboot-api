package com.springapi.demo.services;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.springapi.demo.util.DateUtil;

@Component
public class EmailScheduler {
    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void sendEmail(){
        _LOGGER.info("Scheduled email");
        //currently disabled to save calls
        //uncomment to send calls
        //emailService.sendEmail(String.format("test at %s", DateUtil.getCurrentTime()));
    }

}
