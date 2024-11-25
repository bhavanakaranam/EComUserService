package com.scaler.ecomuserservice.security.service;

import com.scaler.ecomuserservice.models.ECom_User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomSpringUserDetails implements UserDetails
{
    private ECom_User user;

    public CustomSpringUserDetails(ECom_User user)
    {
        this.user = user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    public boolean isAccountNonExpired()
    {
        return true;
    }

    public boolean isAccountNonLocked()
    {
        return true;
    }

    public boolean isCredentialNonExpired()
    {
        return true;
    }

    public boolean isEnabled()
    {
        return true;
    }

}
