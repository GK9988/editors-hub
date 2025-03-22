package org.garvk.authservice.controller;

import org.garvk.authservice.exception.InvalidCredentialsException;
import org.garvk.authservice.exception.TokenValidationException;
import org.garvk.authservice.model.AuthDto;
import org.garvk.authservice.model.User;
import org.garvk.authservice.model.UserDto;
import org.garvk.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody User aInUser){
        User lUser = authService.registerUser(aInUser);

        UserDto aOutUser = new UserDto(lUser);

        return new ResponseEntity<>(aOutUser, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDto aInAuthDto){

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        aInAuthDto.getUsername(),
                        aInAuthDto.getPassword()
                )
        );

        if(auth.isAuthenticated()){
            String token = authService.generateToken(aInAuthDto.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }

        throw new InvalidCredentialsException();
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String aInToken){
        boolean aOutResult = authService.validateToken(aInToken);

        if(aOutResult){
            return new ResponseEntity<>(aOutResult, HttpStatus.OK);
        } else{
            throw new TokenValidationException("Invalid Credentials/token");
        }
    }

}
