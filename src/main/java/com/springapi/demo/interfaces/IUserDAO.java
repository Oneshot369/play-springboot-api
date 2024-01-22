package com.springapi.demo.interfaces;

import com.springapi.demo.api.model.UserModel;

public interface IUserDAO {
    public UserModel getUserByID(int id);
}
