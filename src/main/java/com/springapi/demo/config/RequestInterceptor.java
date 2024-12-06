package com.springapi.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    
    private static final Logger _LOGGER = LogManager.getLogger(SpringBootApplication.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //* Business logic just when the request is received and intercepted by this interceptor before reaching the controller
        try {
            String requestID = request.getRequestId();
            _LOGGER.info("NEW REQUEST ID: " + requestID);
            _LOGGER.info(requestID + "\tMethod Type: " + request.getMethod());
            _LOGGER.info(requestID+ "\tRequest URL: " + request.getRequestURI());
            if(request.getHeader("Authorization") != null)
                _LOGGER.info(requestID + "\tMethod has an Authorization Header");
            _LOGGER.info(requestID + "\tStart time in milliseconds: " + System.currentTimeMillis());

        }
        //* If the Exception is caught, this method will return false
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
        try {
            String requestID = request.getRequestId();
            _LOGGER.info("AFTER REQUEST ID: " + requestID);
            _LOGGER.info(requestID + "\tResponse Status: " + response.getStatus());
            _LOGGER.info(requestID + "\tResponse Status: " + response.getContentType());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
      try {
        String requestID = request.getRequestId();
        _LOGGER.info("Request and Response is completed for request: " + requestID);
        _LOGGER.info(requestID + "\tEnd time in milliseconds: " + System.currentTimeMillis());

      } catch (Exception e) {
        e.printStackTrace();
      }
      
   }
}
