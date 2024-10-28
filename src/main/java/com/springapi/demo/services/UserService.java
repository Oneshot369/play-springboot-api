package com.springapi.demo.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springapi.demo.config.JwtAuthenticationFilter;
import com.springapi.demo.config.JwtTokenProvider;
import com.springapi.demo.model.dataObject.ConstraintModel;
import com.springapi.demo.model.dataObject.LoginAttemptModel;
import com.springapi.demo.model.dataObject.UserLocationModel;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.model.entity.ConstraintEntity;
import com.springapi.demo.model.entity.UserEntity;
import com.springapi.demo.model.entity.UserLocationEntities;
import com.springapi.demo.repos.ConstraintRepositoryInterface;
import com.springapi.demo.repos.LocationRepositoryInterface;
import com.springapi.demo.repos.UserRepositoryInterface;
import com.springapi.demo.util.AuthorityUtil;
import com.springapi.demo.util.DateUtil;
import com.springapi.demo.util.JsonFormatter;

@Service
public class UserService{
    
    @Autowired
    private UserRepositoryInterface userRepo;
    @Autowired
    private LocationRepositoryInterface locationRepo;
    @Autowired
    private ConstraintRepositoryInterface constraintRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * gets one user by ID
     * if none is found then return new UserModel(-1, null, null, -1)
     * @param id
     * @param user 
     * @return
     */
    public String getUserById(Long id, User user) {
        if(!AuthorityUtil.hasAuthorities(user)){
            return JsonFormatter.makeJsonResponse(HttpStatus.FORBIDDEN, "This is a admin feature");
        }
        UserEntity userEntity = userRepo.getById(id);
        //check the response
        try{
            userEntity.getUsername();
        }
        catch(Exception e){
            return JsonFormatter.makeJsonResponse(HttpStatus.NOT_FOUND, String.format("User not found by ID: %d", id));
        }

        //convert from entity to user
        UserModel userModel = new UserModel();

        userModel.convertValuesModel(userEntity);

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, userModel);
    }

    /**
     * Gets one user by name
     * @param username
     * @param auth 
     * @return
     */
    public String getUserByName(String username, User user){
        if(!AuthorityUtil.hasAuthorities(user)){
            return JsonFormatter.makeJsonResponse(HttpStatus.FORBIDDEN, "This is a admin feature");
        }
        List<UserEntity> userEntityList = userRepo.findByUsername(username);
        if(userEntityList.isEmpty()){
            return JsonFormatter.makeJsonResponse(HttpStatus.NOT_FOUND, "User not found by: " + username);
        }

        UserModel model = new UserModel().convertValuesModel(userEntityList.get(0));

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, model);
    }

    /**
     * gets all users
     * @return
     */
    public String getAllUsers(User user){
        if(!AuthorityUtil.hasAuthorities(user)){
            return JsonFormatter.makeJsonResponse(HttpStatus.FORBIDDEN, "This is a admin feature");
        }
        List<UserEntity> userEntityList = userRepo.findAll();
        if(userEntityList.isEmpty()){
            return JsonFormatter.makeJsonResponse(HttpStatus.NOT_FOUND, "No users found");
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
        //get current date as string
        model.setLastLogin(DateUtil.getCurrentTime());
        entity.convertValuesModel(model);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        UserEntity userID = userRepo.save(entity);
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, String.format("User saved with Id: %s", userID.getId()));
    }

    //-------------------------LOCATIONS----------------------------------

    public String saveLocationToUser(UserLocationModel userLocation, int userId) {
        UserLocationEntities userLocationEntities = new UserLocationEntities();
        userLocationEntities.convertValuesModel(userLocation);

        locationRepo.saveLocationToUser(userLocation.getLat(), userLocation.getLon(), userLocation.getName(), userId);

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, String.format("Location was saved for user: %d", userId));
    }

    public String updateLocation(UserLocationModel userLocation) {
        locationRepo.updateLocationById(userLocation.getName(), userLocation.getLat(), userLocation.getLon(), userLocation.getId());

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, String.format("Location was Updated."));
    }

    public String deleteLocation(int locationId) {
        locationRepo.deleteLocationById(locationId);

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, String.format("Location was deleted."));
    }

    //-----------------------------Constraints-----------------------------

    public String saveConstraintToUser(ConstraintModel userConstraint, int locationId) {
        ConstraintEntity constraintEntity = new ConstraintEntity();
        constraintEntity.convertValuesModel(userConstraint);

        constraintRepo.saveConstraintToLocation(locationId, constraintEntity.getCondition().toString(), constraintEntity.getVal(), constraintEntity.isGreaterThan(),constraintEntity.getName());
        // TODO Auto-generated method stub
        return JsonFormatter.makeJsonResponse(HttpStatus.OK, String.format("Constraint was saved for location: %d", locationId));
    }

    public String updateConstraint(ConstraintModel userLocation) {
        constraintRepo.updateConstraintById(userLocation.getName(), userLocation.getCondition().toString(), userLocation.getVal(), userLocation.isGreaterThan(), userLocation.getId());

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, String.format("Constraint was updated."));
    }

    public String deleteConstraint(int locationId) {
        constraintRepo.deleteById(locationId);

        return JsonFormatter.makeJsonResponse(HttpStatus.OK, String.format("Constraint was deleted."));
    }

    public String attemptLogin(LoginAttemptModel loginAttempt) {
        try{
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginAttempt.getUsername(), loginAttempt.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            String jwt = jwtTokenProvider.generateToken(auth);
            return JsonFormatter.makeJsonResponse(HttpStatus.OK, jwt);
        } 
        catch(Exception e){
            return JsonFormatter.makeJsonResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    
}
