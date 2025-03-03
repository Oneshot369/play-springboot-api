package com.springapi.demo.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.springapi.demo.model.dataObject.UserLocationModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "locations", schema = "personal")
public class UserLocationEntities {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    private double lon;
    private double lat;
    private String name;

    //each locations under a user can have a list of constraints
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="location_id")
    private List<ConstraintEntity> constraints;

    // @ManyToOne
    // @JoinColumn(name="user_id")
    // private UserEntity user;

    public UserLocationEntities convertValuesModel(UserLocationModel model){
        id = model.getId();
        lon = model.getLon();
        lat = model.getLat();
        name = model.getName();
        //convert each entity to a model
        List<ConstraintEntity> constraintEntitiesList = new ArrayList<>();
        if(model.getConstraints() != null)
            model.getConstraints().forEach((e) -> {constraintEntitiesList.add(new ConstraintEntity().convertValuesModel(e));});
        constraints = constraintEntitiesList;
        return this;
    }
}
