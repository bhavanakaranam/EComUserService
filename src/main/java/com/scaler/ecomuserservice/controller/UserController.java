package com.scaler.ecomuserservice.controller;

import com.scaler.ecomuserservice.dtos.UserDTO;
import com.scaler.ecomuserservice.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Getter
@Setter
@Controller
public class UserController
{
    private UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUserDetails(@PathVariable("id") Long userId)
    {
        UserDTO userDTO = userService.getUserDetails(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/users/{id}/{roleId}")
    public ResponseEntity setUserRoles(@PathVariable("id") Long userId, @PathVariable("roleId") List<Long> roleIds)
    {
        this.userService.setUserRoles(userId, roleIds);
        return ResponseEntity.ok(200);
    }
}
