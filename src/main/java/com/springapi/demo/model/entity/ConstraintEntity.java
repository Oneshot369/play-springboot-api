package com.springapi.demo.model.entity;

import java.util.List;

import com.springapi.demo.model.dataObject.ConstraintModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "constraints", schema = "personal")
public class ConstraintEntity {
    @Id
    @GeneratedValue
    @Column(name = "constraint_id", updatable = false, nullable = false)
    private int id;
    @Column(name = "locationID")
    private int locationId;
    private String name;
    private String condition;
    private String val;
    @Column(name = "above_below")
    private boolean isGreaterOrLessThan;

    @ManyToOne
    @JoinColumn(name="location_id")
    private UserLocationEntities userLocationEntities;


    public ConstraintEntity convertValuesModel(ConstraintModel model){
        id = model.getId();
        name = model.getName();
        condition = model.getCondition();
        val = model.getVal();
        isGreaterOrLessThan = model.isGreaterOrLessThan();
        return this;
    }
}
