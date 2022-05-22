package com.spring.pidev.controller;

import com.spring.pidev.model.loginRequest;
import com.spring.pidev.model.User;
import com.spring.pidev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class AppController {

    @Autowired
    private UserService userService;
    private List<User> validUsers = new ArrayList<>();

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public User userLogin(@RequestBody loginRequest loginRequest, HttpServletResponse response) {
        Optional<User> user = userService.retrieveAllUsers()
                .stream()
                .filter(u -> u.getDisplayName().equalsIgnoreCase(loginRequest.getName()))
                .findFirst();

        if (user.isPresent()) {
            return user.get();
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }



    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public List<User> listUsers() {
        return  userService.retrieveAllUsers();
    }


}
