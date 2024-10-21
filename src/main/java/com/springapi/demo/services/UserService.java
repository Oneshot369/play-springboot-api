package com.springapi.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springapi.demo.interfaces.UserRepositoryInterface;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.model.entity.UserEntity;
import com.springapi.demo.util.JsonFormatter;

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
        UserEntity userEntity = userRepo.getById(id);
        //check the response
        if(userEntity == null){

            return JsonFormatter.makeJsonResponse(HttpStatus.NOT_FOUND, String.format("User not found by ID: %d", id));
        }
        //convert from entity to user
        UserModel userModel = new UserModel();

        userModel.convertValuesModel(userEntity);

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, userModel);
    }

    /**
     * Gets one user by name
     * @param UserName
     * @return
     */
    public String getUserByName(String UserName){
        List<UserEntity> userEntityList = userRepo.findByUsername(UserName);
        if(userEntityList.isEmpty()){
            return JsonFormatter.makeJsonResponse(HttpStatus.NOT_FOUND, userEntityList);
        }

        UserModel model = new UserModel().convertValuesModel(userEntityList.get(0));

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, model);
    }

    /**
     * gets all users
     * @return
     */
    public String getAllUsers(){
        List<UserEntity> userEntityList = userRepo.findAll();
        if(userEntityList.isEmpty()){
            return JsonFormatter.makeJsonResponse(HttpStatus.NOT_FOUND, userEntityList);
        }
        List<UserModel> userModels = new ArrayList<>();
        for(UserEntity e : userEntityList){
            userModels.add(new UserModel().convertValuesModel(e));
        }
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, userEntityList);
    }

     /**
     * gets one user by ID
     * if none is found then return new UserModel(-1, null, null, -1)
     * @param id
     * @return
     */
    public String saveUser(UserModel model) {
        UserEntity entity = new UserEntity();
        
        entity.convertValuesModel(model);
        UserEntity userID = userRepo.save(entity);
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, String.format("User saved with Id: %s", userID.getId()));
    }
    
}
