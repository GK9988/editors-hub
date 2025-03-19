package org.garvk.authservice.service;

import org.garvk.authservice.exception.AuthServiceException;
import org.garvk.authservice.exception.EmailAlreadyExistsException;
import org.garvk.authservice.exception.UsernameAlreadyExistsException;
import org.garvk.authservice.model.User;
import org.garvk.authservice.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User aInUser){
        User userByUsername = userRepo.findByUsername(aInUser.getUsername()).orElse(null);
        if(null != userByUsername){
            throw new UsernameAlreadyExistsException(aInUser.getUsername());
        }

        User userByEmail = userRepo.findByEmail(aInUser.getEmail()).orElse(null);
        if(null != userByEmail){
            throw new EmailAlreadyExistsException(aInUser.getEmail());
        }

        aInUser.setPasswordHash(passwordEncoder.encode(aInUser.getPasswordHash()));

        try{
            userRepo.save(aInUser);
        } catch (Exception e) {
            throw new AuthServiceException(e.getMessage(), e);
        }

        logger.info("Added User: {}", aInUser.getUsername());

        return aInUser;
    }



}
