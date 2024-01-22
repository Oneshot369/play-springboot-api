package com.springapi.demo.services;

import org.springframework.stereotype.Service;

import com.springapi.demo.api.model.UserModel;
import com.springapi.demo.interfaces.IUserDAO;

@Service
public class UserService {
    private IUserDAO userDAO;
 
    public UserService() {
        this.userDAO = new UserMockData();
    }
    /**
     * gets one user by ID
     * if none is found then return new UserModel(-1, null, null, -1)
     * @param id
     * @return
     */
    public UserModel getUserById(Integer id) {
        // TODO Auto-generated method stub
        return userDAO.getUserByID(id);
    }
    
}
