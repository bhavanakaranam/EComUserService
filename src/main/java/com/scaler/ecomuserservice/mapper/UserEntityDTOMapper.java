package com.scaler.ecomuserservice.mapper;

import com.scaler.ecomuserservice.dtos.UserDTO;
import com.scaler.ecomuserservice.models.ECom_User;

public class UserEntityDTOMapper
{
    public static UserDTO getUserDTOFromUserEntity(ECom_User user)
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
