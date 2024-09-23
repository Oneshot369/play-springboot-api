package com.springapi.demo.model.entity;

import com.springapi.demo.model.dataObject.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int age;

    public UserEntity(Long userId, String firstName, String lastName, int age) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public void convertValuesModel(UserModel userModel){
        userId = userModel.getId();
        firstName = userModel.getFirstName();
        lastName = userModel.getLastName();
        age = userModel.getAge();
        username = userModel.getUsername();
        password = userModel.getPassword();
    }
}
