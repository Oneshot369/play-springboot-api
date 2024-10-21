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
    public String cod;
    public int message;
    public int cnt;
    public List<ForecastItemModel> list;
    public CityModel city;
}
