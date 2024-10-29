package com.scaler.ecomuserservice.service;

import com.scaler.ecomuserservice.dtos.UserDTO;
import com.scaler.ecomuserservice.models.Role;
import com.scaler.ecomuserservice.models.User;
import com.scaler.ecomuserservice.repository.RoleRepository;
import com.scaler.ecomuserservice.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Getter
@Setter
public class UserService
{
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDTO getUserDetails(Long userId)
    {
        Optional<User> users = this.userRepository.findById(userId);

        if(users.isEmpty())
            return null;

        User user = users.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        return userDTO;

    }

    public void setUserRoles(Long userId, List<Long> roleIds)
    {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isEmpty())
            return;

        Set<Role> roles = this.roleRepository.findByIdIn(roleIds);

        user.get().setRoles(roles);
    }
}
