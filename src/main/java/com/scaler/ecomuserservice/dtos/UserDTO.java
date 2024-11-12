package com.scaler.ecomuserservice.dtos;

import com.scaler.ecomuserservice.models.ECom_User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO
{
    private Long id;

    private String email;

    public static UserDTO from(ECom_User savedUser)
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setEmail(savedUser.getEmail());
        return userDTO;
    }
}
