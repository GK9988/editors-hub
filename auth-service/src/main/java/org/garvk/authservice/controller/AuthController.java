package org.garvk.authservice.controller;

import org.garvk.authservice.model.User;
import org.garvk.authservice.model.UserDto;
import org.garvk.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody User aInUser){
        User lUser = authService.registerUser(aInUser);

        UserDto aOutUser = new UserDto(lUser);

        return new ResponseEntity<>(aOutUser, HttpStatus.CREATED);
    }

}
