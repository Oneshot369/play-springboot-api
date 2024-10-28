package com.springapi.demo.model.dataObject;

import java.util.ArrayList;
import java.util.List;

import com.springapi.demo.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int age;
    private int userId;
    private String lastLogin;
    private List<UserLocationModel> locations;

    public UserModel convertValuesModel(UserEntity userEntity){
        id = userEntity.getId();
        firstName = userEntity.getFirstName();
        lastName = userEntity.getLastName();
        age = userEntity.getAge();
        username = userEntity.getUsername();
        password = userEntity.getPassword();
        lastLogin = userEntity.getLastLogin();
        List<UserLocationModel> locationList = new ArrayList<>();
        if(userEntity.getLocations() != null)
            userEntity.getLocations().forEach((e) -> {locationList.add(new UserLocationModel().convertValuesModel(e));});
        locations = locationList;
        return this;
    }
}