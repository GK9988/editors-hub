package org.garvk.authservice.service;

import org.garvk.authservice.model.AbstractUserDetailsDto;
import org.garvk.authservice.model.User;
import org.garvk.authservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AbstractUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User lUser = userRepo.findByUsername(username).orElse(null);

        if(null == lUser) throw new UsernameNotFoundException("Username " + username + " not found");

        return new AbstractUserDetailsDto(lUser);
    }
}
