package org.garvk.authservice.service;

import org.garvk.authservice.exception.AuthServiceException;
import org.garvk.authservice.exception.EmailAlreadyExistsException;
import org.garvk.authservice.exception.UsernameAlreadyExistsException;
import org.garvk.authservice.model.Profile;
import org.garvk.authservice.model.User;
import org.garvk.authservice.repo.ProfileRepo;
import org.garvk.authservice.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public String generateToken(String aInUsername){
        return jwtUtils.generateToken(aInUsername);
    }

    public boolean validateToken(String aInToken, String aInUserName){
        return jwtUtils.validateToken(aInToken, aInUserName);
    }

    @Transactional
    public User registerUser(User aInUser){

        if(null != aInUser.getUsername()){
            User userByUsername = userRepo.findByUsername(aInUser.getUsername()).orElse(null);
            if(null != userByUsername){
                throw new UsernameAlreadyExistsException(aInUser.getUsername());
            }
        }

        if(null != aInUser.getEmail()){
            User userByEmail = userRepo.findByEmail(aInUser.getEmail()).orElse(null);
            if(null != userByEmail){
                throw new EmailAlreadyExistsException(aInUser.getEmail());
            }
        }

        try{
            Profile lProfile = aInUser.getProfile();
            aInUser.setProfile(null);

            aInUser.setPasswordHash(passwordEncoder.encode(aInUser.getPasswordHash()));

            User aOutUser = userRepo.save(aInUser);

            if(null != lProfile){
                lProfile.setUser(aOutUser);

                profileRepo.save(lProfile);

                aOutUser.setProfile(lProfile);
            }

            logger.info("Added User: {}, with id: {}", aOutUser.getUsername(), aOutUser.getId());

            return aOutUser;
        } catch (Exception e) {
            throw new AuthServiceException(e.getMessage(), e);
        }
    }



}
