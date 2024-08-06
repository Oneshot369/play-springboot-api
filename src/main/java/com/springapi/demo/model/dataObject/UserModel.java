package com.springapi.demo.model.dataObject;

import com.springapi.demo.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long userId;
    private String firstName;
    private String lastName;
    private int age;

    public void convertValuesEntity(UserEntity userEntity){
        userId = userEntity.getUserId();
        firstName = userEntity.getFirstName();
        lastName = userEntity.getLastName();
        age = userEntity.getAge();
    }
}