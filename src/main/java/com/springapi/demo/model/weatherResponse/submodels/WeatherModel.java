package com.springapi.demo.model.weatherResponse.submodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherModel {
    private int id;
    private String main;
    private String description;
    private String icon;
}
