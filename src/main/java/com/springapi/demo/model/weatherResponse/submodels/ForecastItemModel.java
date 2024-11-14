package com.springapi.demo.model.weatherResponse.submodels;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastItemModel {
    private long dt;
    private MainWeatherModel main;
    private List<WeatherModel> weather;
    private CloudsModel clouds;
    private WindModel wind;
    private int visibility;
    private double pop;
    private SysModel sys;
    private String dt_txt;
}
