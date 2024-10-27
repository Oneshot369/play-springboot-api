package com.springapi.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springapi.demo.model.entity.UserEntity;
import com.springapi.demo.repos.UserRepositoryInterface;
import com.springapi.demo.util.DateUtil;

@Service
public class UserSecurityService implements UserDetailsService{
    
    @Autowired
    private UserRepositoryInterface userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        List<UserEntity> users = userRepo.findByUsername(username);
        if(users.isEmpty() || users.size() > 1){
            return null;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        //add role of admin if they are one
        if(users.get(0).isAdmin())
            authorities.add((GrantedAuthority) () -> "Admin");

        UserDetails userDetails = new User(users.get(0).getUsername(), users.get(0).getPassword(), authorities);
        //update last login time
        userRepo.updateLastLoginTime(DateUtil.getCurrentTime(), users.get(0).getId());
        return userDetails;
    }
}
