package org.garvk.authservice.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AbstractUserDetailsDto implements UserDetails {

    private String username;
    private String password;

    public AbstractUserDetailsDto(String aInUsername, String aInPassword){
        this.username = aInUsername;
        this.password = aInPassword;
    }

    public AbstractUserDetailsDto(User aInUser){
        this.username = aInUser.getUsername();
        this.password = aInUser.getPasswordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
