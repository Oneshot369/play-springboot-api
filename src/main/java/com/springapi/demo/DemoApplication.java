package com.springapi.demo;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
@EnableScheduling
public class DemoApplication {

	private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);
	public static void main(String[] args) {
		_LOGGER.info("Swagger UI at URL: " + "http://localhost:8080/swagger-ui/index.html#/");
		SpringApplication.run(DemoApplication.class, args);
	}

}
