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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;


import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

@Service
@Getter
@Setter
public class AuthService
{
    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserDTO> login(String email, String password) throws UserNotFoundException, InvalidCredentialException
    {
        // Get user details from DB
        Optional<ECom_User> userOptional = this.userRepository.findByEmail(email);
        if(userOptional.isEmpty())
            throw new UserNotFoundException("User with "+email+" not found.");

        ECom_User user = userOptional.get();
        if(!this.bCryptPasswordEncoder.matches(password, user.getPassword()))
            throw new InvalidCredentialException("Invalid userId or password");

        // JWT Token Generation
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Add the claims
        Map<String, Object> jsonForJWT = new HashMap<>();
        jsonForJWT.put("userId", user.getId());
        jsonForJWT.put("roles", user.getRoles());
        jsonForJWT.put("createdAt", new Date());
        jsonForJWT.put("expiryAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));

        String token = Jwts.builder().addClaims(jsonForJWT).signWith(key).compact();

        // Random alphanumeric String
        // String token = RandomStringUtils.randomAlphanumeric(10);

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
        user.setPassword(this.bCryptPasswordEncoder.encode(password));
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
