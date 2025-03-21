package com.springapi.demo.services;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailScheduler {
    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    @Autowired
    private EmailService emailService;

    //60000 == 1 min
    @Scheduled(fixedRate = 60000)
    public void sendEmail(){
        _LOGGER.info("Scheduled email");
        //currently disabled to save calls as we have a limit

        //uncomment this line to send calls
        //emailService.sendEmailsToUsers();
    }

}
