package com.springapi.demo.model.weatherResponse;

import java.util.List;

import com.springapi.demo.model.weatherResponse.submodels.CloudsModel;
import com.springapi.demo.model.weatherResponse.submodels.CoordModel;
import com.springapi.demo.model.weatherResponse.submodels.MainWeatherModel;
import com.springapi.demo.model.weatherResponse.submodels.RainModel;
import com.springapi.demo.model.weatherResponse.submodels.SysModel;
import com.springapi.demo.model.weatherResponse.submodels.WeatherModel;
import com.springapi.demo.model.weatherResponse.submodels.WindModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentWeatherModel {
    private CoordModel coord;
    private List<WeatherModel> weather;
    private String base;
    private MainWeatherModel main;
    private int visibility;
    private WindModel wind;
    private RainModel rain;
    private CloudsModel clouds;
    private long dt;
    private SysModel sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
}
