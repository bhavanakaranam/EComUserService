package com.scaler.ecomuserservice.service;

import com.scaler.ecomuserservice.dtos.UserDTO;
import com.scaler.ecomuserservice.exception.InvalidCredentialException;
import com.scaler.ecomuserservice.exception.InvalidTokenException;
import com.scaler.ecomuserservice.exception.UserNotFoundException;
import com.scaler.ecomuserservice.mapper.UserEntityDTOMapper;
import com.scaler.ecomuserservice.models.ECom_User;
import com.scaler.ecomuserservice.models.Session;
import com.scaler.ecomuserservice.models.SessionStatus;
import com.scaler.ecomuserservice.repository.SessionRepository;
import com.scaler.ecomuserservice.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
public class AuthService
{
    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    @Autowired
    public AuthService(UserRepository userRepository, SessionRepository sessionRepository)
    {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public ResponseEntity<UserDTO> login(String email, String password) throws UserNotFoundException, InvalidCredentialException
    {
        // Get user details from DB
        Optional<ECom_User> userOptional = this.userRepository.findByEmail(email);
        if(userOptional.isEmpty())
            throw new UserNotFoundException("User with "+email+" not found.");

        ECom_User user = userOptional.get();
        if(!user.getPassword().equals(password))
            throw new InvalidCredentialException("Invalid userId or password");

        String token = RandomStringUtils.randomAlphanumeric(10);

        // Create session
        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        session.setStatus(SessionStatus.ACTIVE);
        session.setStartTime(new Date());
        this.sessionRepository.save(session);

        // Generating the response
        UserDTO userDTO = UserEntityDTOMapper.getUserDTOFromUserEntity(user);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, token);


        return new ResponseEntity<>(userDTO, headers, HttpStatus.OK);
    }

    public UserDTO signup(String email, String password)
    {
        ECom_User user = new ECom_User();
        user.setEmail(email);
        user.setPassword(password);
        ECom_User savedUser = userRepository.save(user);
        return UserDTO.from(savedUser);
    }


    public ResponseEntity<Void> logout(String token, Long userId)
    {
        Optional<Session> sessionOptional = this.sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty())
            return null;
        Session session = sessionOptional.get();
        session.setStatus(SessionStatus.ENDED);
        this.sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }

    public SessionStatus validate(Long userId, String token) throws InvalidTokenException
    {
        // verifying from DB if session exists
        Optional<Session> sessionOptional = this.sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty() || sessionOptional.get().getStatus().equals(SessionStatus.ENDED))
        {
            throw new InvalidTokenException("Session token "+token+" not valid. ");
        }

        return SessionStatus.ACTIVE;
    }

    public List<Session> getAllSessions()
    {
        return this.sessionRepository.findAll();
    }

    public List<ECom_User> getAllUsers()
    {
        return this.userRepository.findAll();
    }
}