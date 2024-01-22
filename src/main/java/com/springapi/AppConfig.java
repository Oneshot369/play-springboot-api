package com.springapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springapi.demo.interfaces.IUserDAO;
import com.springapi.demo.services.UserMockData;

@Configuration
public class AppConfig {
    @Bean
    public IUserDAO getUserDAO(){
        return new UserMockData();
    }
}
