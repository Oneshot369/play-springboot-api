package com.springapi.demo.model.dataObject;

import com.springapi.demo.model.entity.ConstraintEntity;
import com.springapi.demo.model.weatherResponse.WeatherTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {

    private String jwt;
    private boolean admin;

}

