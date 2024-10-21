package com.springapi.demo.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.springapi.demo.model.dataObject.UserModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "users", schema = "personal")
public class UserEntity {
    @Id
    @GeneratedValue
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int age;
    //user has a list of locations
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private List<UserLocationEntities> locations;

    public UserEntity(Long id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public UserEntity convertValuesModel(UserModel model){
        id = model.getId();
        firstName = model.getFirstName();
        lastName = model.getLastName();
        age = model.getAge();
        username = model.getUsername();
        password = model.getPassword();
        List<UserLocationEntities> locationList = new ArrayList<>();
        if(model.getLocations() != null)
            model.getLocations().forEach((e) -> {locationList.add(new UserLocationEntities().convertValuesModel(e));});
        locations = locationList;
        return this;
    }
}
