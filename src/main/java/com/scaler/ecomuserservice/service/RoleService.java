package com.scaler.ecomuserservice.service;

import com.scaler.ecomuserservice.dtos.CreateRoleRequestDTO;
import com.scaler.ecomuserservice.models.Role;
import com.scaler.ecomuserservice.repository.RoleRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class RoleService
{
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    public void addRole(CreateRoleRequestDTO role)
    {
        if(role.getRoleName() == null || role.getRoleName().isEmpty())
            return;

        Role newRole = new Role();
        newRole.setRoleName(role.getRoleName());
        this.roleRepository.save(newRole);
    }
}
