package org.garvk.authservice.controller;

import org.garvk.authservice.exception.TokenValidationException;
import org.garvk.authservice.model.AuthDto;
import org.garvk.authservice.model.User;
import org.garvk.authservice.model.UserDto;
import org.garvk.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDto aInAuthDto){
        String token = authService.generateToken(aInAuthDto.getUsername());

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String aInToken, @RequestBody AuthDto aInAuthDto){
        boolean aOutResult = authService.validateToken(aInToken, aInAuthDto.getUsername());

        if(aOutResult){
            return new ResponseEntity<>(aOutResult, HttpStatus.OK);
        } else{
            throw new TokenValidationException("Invalid Credentials/token");
        }
    }

}
