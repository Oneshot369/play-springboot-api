package com.springapi.demo.model.weatherResponse.submodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordModel {
    private double lon;
    private double lat;
}
