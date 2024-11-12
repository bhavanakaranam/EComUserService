package com.scaler.ecomuserservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto
{
    private String password;

    private String email;
}
