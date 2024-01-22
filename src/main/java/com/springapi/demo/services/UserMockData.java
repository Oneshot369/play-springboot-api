package com.springapi.demo.services;

import java.util.ArrayList;
import java.util.List;

import com.springapi.demo.api.model.UserModel;
import com.springapi.demo.interfaces.IUserDAO;

public class UserMockData implements IUserDAO{

    private List<UserModel> users;

    public UserMockData(){
        users = new ArrayList<>();
        //add some mock data
        users.add(new UserModel(1, "Josh", "Peck", 19));
        users.add(new UserModel(2, "Zach", "Peck", 21));
        users.add(new UserModel(3, "Ace", "Lake", 20));
    }

    @Override
    public UserModel getUserByID(int id) {
        //get the user in the list that matches the ID given
        return users.stream()
        .filter(user -> user.getUserId() == id).
        findFirst()
        .orElse(new UserModel(-1, null, null, -1));
    }
    
}
