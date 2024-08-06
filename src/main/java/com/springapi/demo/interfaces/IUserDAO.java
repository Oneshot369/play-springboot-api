package com.springapi.demo.interfaces;

import com.springapi.demo.model.dataObject.UserModel;

public interface IUserDAO {
    public UserModel getUserByID(int id);
}
