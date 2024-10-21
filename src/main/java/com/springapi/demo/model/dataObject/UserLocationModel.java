package com.springapi.demo.model.dataObject;

import java.util.ArrayList;
import java.util.List;

import com.springapi.demo.model.entity.ConstraintEntity;
import com.springapi.demo.model.entity.UserLocationEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationModel {
    private int id;
    private double lon;
    private double lat;
    private String name;

    //each locations under a user can have a list of constraints
    private List<ConstraintModel> constraints;


    public UserLocationModel convertValuesModel(UserLocationEntities model){
        id = model.getId();
        lon = model.getLon();
        lat = model.getLat();
        name = model.getName();
        //convert each entity to a model
        List<ConstraintModel> constraintEntitiesList = new ArrayList<>();
        if(model.getConstraints() != null)
            model.getConstraints().forEach((e) -> {constraintEntitiesList.add(new ConstraintModel().convertValuesModel(e));});
        constraints = constraintEntitiesList;
        return this;
    }
}
