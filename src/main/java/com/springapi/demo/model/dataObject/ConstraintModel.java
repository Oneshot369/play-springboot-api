package com.springapi.demo.model.dataObject;

import com.springapi.demo.model.entity.ConstraintEntity;
import com.springapi.demo.model.weatherResponse.WeatherTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintModel {

    private int id;
    private String name;
    private WeatherTypes condition;
    private String val;
    private boolean greaterThan;

    public ConstraintModel convertValuesModel(ConstraintEntity model){
        id = model.getId();
        name = model.getName();
        condition = WeatherTypes.valueOf(model.getCondition());
        val = model.getVal();
        greaterThan = model.isGreaterThan();
        return this;
    }
}

