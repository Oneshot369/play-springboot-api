package com.springapi.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.springapi.demo.data.UserMockData;
import com.springapi.demo.interfaces.IUserDAO;
import com.springapi.demo.interfaces.UserRepositoryInterface;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.model.entity.UserEntity;

@Service
public class UserService {
    
    @Autowired
    private UserRepositoryInterface userDAO;

    /**
     * gets one user by ID
     * if none is found then return new UserModel(-1, null, null, -1)
     * @param id
     * @return
     */
    public UserModel getUserById(Long id) {
        List<UserEntity> userEntityList = userDAO.getByUserId(id);
        //check the response
        if(userEntityList.isEmpty()){
            //we did not find the result
            return new UserModel();
        }
        UserEntity userEntity = userEntityList.get(0);

        //convert from entity to user
        UserModel userModel = new UserModel();
        userModel.convertValuesEntity(userEntity);
        
        return userModel;
    }
    
}
