package com.springapi.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springapi.demo.interfaces.UserRepositoryInterface;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.model.entity.UserEntity;
import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.StatusCode;

@Service
public class UserService {
    
    @Autowired
    private UserRepositoryInterface userRepo;

    /**
     * gets one user by ID
     * if none is found then return new UserModel(-1, null, null, -1)
     * @param id
     * @return
     */
    public String getUserById(Long id) {
        List<UserEntity> userEntityList = userRepo.getByUserId(id);
        //check the response
        if(userEntityList.isEmpty()){

            return JsonFormatter.makeJsonResponse(StatusCode.NOT_FOUND, String.format("User not found by ID: %d", id));
        }
        UserEntity userEntity = userEntityList.get(0);

        //convert from entity to user
        UserModel userModel = new UserModel();

        userModel.convertValuesEntity(userEntity);

        return JsonFormatter.makeJsonResponse(StatusCode.OK, userModel);
    }

    public String getAllUsers(){
        List<UserEntity> userEntityList = userRepo.findAll();
        if(userEntityList.isEmpty()){
            return JsonFormatter.makeJsonResponse(StatusCode.NOT_FOUND, userEntityList);
        }
        return JsonFormatter.makeJsonResponse(StatusCode.OK, userEntityList);
    }

     /**
     * gets one user by ID
     * if none is found then return new UserModel(-1, null, null, -1)
     * @param id
     * @return
     */
    public String saveUser(UserModel id) {
        UserEntity entity = new UserEntity();
        
        entity.convertValuesModel(id);
        UserEntity userID = userRepo.save(entity);
        return JsonFormatter.makeJsonResponse(StatusCode.OK, String.format("User saved with Id: %s", userID.getUserId()));
    }
    
}
