package com.blueisfresh.expenser.controller;

import com.blueisfresh.expenser.dto.UserProfileDto;
import com.blueisfresh.expenser.dto.userSigninDto;
import com.blueisfresh.expenser.dto.userSignupDto;
import com.blueisfresh.expenser.entity.User;
import com.blueisfresh.expenser.repository.UserRepository;
import com.blueisfresh.expenser.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody userSigninDto user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody userSignupDto userSignupRequest) {
        if (userRepository.existsByUsername(userSignupRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is already taken!");
        }

        // Map Dto into a User entity
        User newUser = new User();
        newUser.setUsername(userSignupRequest.getUsername());
        newUser.setEmail(userSignupRequest.getEmail());
        newUser.setPasswordHash(encoder.encode(userSignupRequest.getPassword()));
        newUser.setFullName(userSignupRequest.getFullName());

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!"); // returns 200 OK

    }

    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User userEntity = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found. This should not happen."));

        // Map User entity to the UserProfileDto
        UserProfileDto userProfileDto = new UserProfileDto(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFullName(),
                userEntity.getEmail(),
                userEntity.getCreatedAt().toString()
        );

        return ResponseEntity.ok(userProfileDto);
    }
}
