package com.springapi.demo.model.dataObject;

import com.springapi.demo.model.entity.ConstraintEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintModel {

    private int id;
    private int locationId;
    private String name;
    private String condition;
    private String val;
    private boolean isGreaterOrLessThan;

    public ConstraintModel convertValuesModel(ConstraintEntity model){
        id = model.getId();
        name = model.getName();
        condition = model.getCondition();
        val = model.getVal();
        isGreaterOrLessThan = model.isGreaterOrLessThan();
        return this;
    }
}

