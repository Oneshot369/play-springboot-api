package com.springapi.demo.data;

import java.util.ArrayList;
import java.util.List;

import com.springapi.demo.interfaces.IUserDAO;
import com.springapi.demo.model.dataObject.UserModel;

public class UserMockData implements IUserDAO{

    private List<UserModel> users;

    public UserMockData(){
        users = new ArrayList<>();
        //add some mock data
        users.add(new UserModel(1L, "Josh", "Peck", 19));
        users.add(new UserModel(2L, "Zach", "Peck", 21));
        users.add(new UserModel(3L, "Ace", "Lake", 20));
    }

    @Override
    public UserModel getUserByID(int id) {
        //get the user in the list that matches the ID given
        return users.stream()
        .filter(user -> user.getUserId() == id).
        findFirst()
        .orElse(new UserModel(-1L, null, null, -1));
    }
    
}
