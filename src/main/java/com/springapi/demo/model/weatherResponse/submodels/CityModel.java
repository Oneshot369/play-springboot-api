package com.springapi.demo.model.weatherResponse.submodels;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityModel {
    private long id;
    private String name;
    private CoordModel coord;
    private String country;
    private int population;
    private int timezone;
    private long sunrise;
    private long sunset;
}
