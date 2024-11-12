package com.scaler.ecomuserservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Session extends BaseModel
{
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus status;

    private Date startTime;

    private Date expiryTime;

    private String token;

    @ManyToOne
    private ECom_User user;
}
