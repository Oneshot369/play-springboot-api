package com.springapi.demo.model.weatherResponse.submodels;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastItemModel {
    public long dt;
    public MainWeatherModel main;
    public List<WeatherModel> weather;
    public CloudsModel clouds;
    public WindModel wind;
    public int visibility;
    public double pop;
    public SysModel sys;
    public String dt_txt;
}
