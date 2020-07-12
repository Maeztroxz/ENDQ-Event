package com.example.demo.api.security;

import com.example.demo.api.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContext {
    public UserData getUser() {
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userData;
    }

}
