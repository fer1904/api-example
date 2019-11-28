package com.api.exercise.services;

import com.api.exercise.config.JwtConfig;
import com.api.exercise.dto.UserInput;
import com.api.exercise.entities.User;
import com.api.exercise.repository.UserRepository;
import com.api.exercise.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Objects;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtConfig jwtConfig;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtConfig = jwtConfig;
    }

    public User saveUser(UserInput userInput) {
        User user = userRepository.findByEmail(userInput.getEmail());
        if (Objects.nonNull(user)) {
            throw new EntityExistsException("El correo ya está registrado");
        }
        user = this.createUserToSave(userInput);
        user = userRepository.save(user);

        return user;
    }

    private User createUserToSave(UserInput userInput) {
        User user = new ObjectMapper().convertValue(userInput, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
        if (user.getPhones() != null) {
            user.getPhones().forEach(phone -> phone.setUser(user));
        }
        user.setToken(
                TokenUtils.generateJwt(
                        jwtConfig.getIssuer(),
                        user.getEmail(),
                        jwtConfig.getExpirationTime(),
                        jwtConfig.getSecret()
                )
        );
        return user;
    }
}
