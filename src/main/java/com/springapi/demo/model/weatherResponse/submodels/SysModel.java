package com.springapi.demo.model.weatherResponse.submodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysModel {
    private int type;
    private int id;
    private String country;
    private long sunrise;
    private long sunset;
    private String pod;
}
