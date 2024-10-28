package com.springapi.demo.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthorityUtil {

    public static boolean hasAuthorities(User user){
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        for(GrantedAuthority ga: authorities){
            String s = ga.getAuthority();
            if("ADMIN".equals(ga.getAuthority()))
                return true;
        }
        return false;
    }
}
