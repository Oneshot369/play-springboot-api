package com.springapi.demo.model.weatherResponse.submodels;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RainModel {
    @JsonAlias("1h")
    public double oneHour;
    @JsonAlias("3h")
    public double threeHour;
}
