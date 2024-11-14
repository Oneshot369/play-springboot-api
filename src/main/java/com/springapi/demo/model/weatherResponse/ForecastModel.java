package com.springapi.demo.model.weatherResponse;

import java.util.List;

import com.springapi.demo.model.weatherResponse.submodels.CityModel;
import com.springapi.demo.model.weatherResponse.submodels.ForecastItemModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastModel {
    private String cod;
    private int message;
    private int cnt;
    private List<ForecastItemModel> list;
    private CityModel city;
}
