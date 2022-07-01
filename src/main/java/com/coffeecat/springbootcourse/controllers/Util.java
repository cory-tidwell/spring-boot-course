package com.coffeecat.springbootcourse.controllers;

import com.coffeecat.springbootcourse.model.entity.SiteUser;
import com.coffeecat.springbootcourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component //not controller, only utility class
public class Util {
    @Autowired
    private UserService userService;

    public SiteUser getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //returns authentication info about user currently logged in
        String email = auth.getName(); //get the user email.

        return userService.get(email);
    }
}
