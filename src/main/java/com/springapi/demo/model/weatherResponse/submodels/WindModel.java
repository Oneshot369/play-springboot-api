package com.springapi.demo.model.weatherResponse.submodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WindModel {
    private double speed;
    private int deg;
    private double gust;
}
