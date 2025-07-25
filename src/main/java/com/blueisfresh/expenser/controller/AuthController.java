package com.blueisfresh.expenser.controller;

import com.blueisfresh.expenser.dto.userSigninDto;
import com.blueisfresh.expenser.dto.userSignupDto;
import com.blueisfresh.expenser.entity.User;
import com.blueisfresh.expenser.repository.UserRepository;
import com.blueisfresh.expenser.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtils;

    @PostMapping("/signin")
    public String authenticateUser(@RequestBody userSigninDto user) {
        // Receiving a Dto for signup

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody userSignupDto userSignupRequest) {
        // Receiving a Dto for signup

        if (userRepository.existsByUsername(userSignupRequest.getUsername())) {
            return "Error: Username is already taken!";
        }

        // Map Dto into a User entity
        User newUser = new User();
        newUser.setUsername(userSignupRequest.getUsername());
        newUser.setEmail(userSignupRequest.getEmail());
        newUser.setPasswordHash(encoder.encode(userSignupRequest.getPassword()));
        newUser.setFullName(userSignupRequest.getFullName());

        userRepository.save(newUser);
        return "User registered successfully!";
    }
}
