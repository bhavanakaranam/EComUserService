package com.scaler.ecomuserservice.controller;

import com.scaler.ecomuserservice.dtos.CreateRoleRequestDTO;
import com.scaler.ecomuserservice.service.RoleService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Getter
@Setter
@Controller
public class RoleController
{
    private RoleService roleService;

    public RoleController(RoleService roleService)
    {
        this.roleService = roleService;
    }

    @PostMapping("/roles/createRole")
    public ResponseEntity addRole(CreateRoleRequestDTO roleRequestDTO)
    {
        this.roleService.addRole(roleRequestDTO);
        return ResponseEntity.ok(200);
    }
}
