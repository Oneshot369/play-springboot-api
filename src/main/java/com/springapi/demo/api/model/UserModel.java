package com.springapi.demo.api.model;


import lombok.Data;

@Data
public class UserModel {
    private int userId;
    private String firstName;
    private String lastName;
    private int age;

    public UserModel(int userId, String firstName, String lastName, int age) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

}