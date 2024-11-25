package com.scaler.ecomuserservice.security.service;

import com.scaler.ecomuserservice.models.ECom_User;
import com.scaler.ecomuserservice.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomSpringUserDetailsService implements UserDetailsService
{
    private UserRepository userRepository;

    public CustomSpringUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ECom_User> user = userRepository.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User details with given username is not found");
        }
        ECom_User savedUser = user.get();
        return new CustomSpringUserDetails(savedUser);
    }
}
