package com.scaler.ecomuserservice.controller;

import com.scaler.ecomuserservice.dtos.*;
import com.scaler.ecomuserservice.exception.InvalidCredentialException;
import com.scaler.ecomuserservice.exception.InvalidTokenException;
import com.scaler.ecomuserservice.exception.UserNotFoundException;
import com.scaler.ecomuserservice.models.ECom_User;
import com.scaler.ecomuserservice.models.Session;
import com.scaler.ecomuserservice.models.SessionStatus;
import com.scaler.ecomuserservice.service.AuthService;
import lombok.Getter;
import lombok.Setter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Getter
@Setter
@RequestMapping("/auth")
public class AuthController
{
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDto requestDto) throws UserNotFoundException, InvalidCredentialException {
        return this.authService.login(requestDto.getEmail(), requestDto.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto)
    {
        return this.authService.logout(logoutRequestDto.getToken(), logoutRequestDto.getUserId());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupRequestDto requestDto)
    {
        UserDTO userDTO = this.authService.signup(requestDto.getEmail(), requestDto.getPassword());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequestDto requestDto) throws InvalidTokenException
    {
        SessionStatus sessionStatus = this.authService.validate(requestDto.getUserId(), requestDto.getToken());
        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }

    @GetMapping("/session")
    public ResponseEntity<List<Session>> getAllSessions()
    {
        List<Session> sessions = this.authService.getAllSessions();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ECom_User>> getAllUsers()
    {
        List<ECom_User> users = this.authService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
