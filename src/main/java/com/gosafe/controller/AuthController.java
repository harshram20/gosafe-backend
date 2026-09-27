package com.gosafe.controller;

import com.gosafe.model.User;
import com.gosafe.payload.*;
import com.gosafe.repository.UserRepository;
import com.gosafe.security.jwt.JwtUtils;
import com.gosafe.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
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
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                userDetails.getEmergencyContactName(),
                userDetails.getEmergencyContactPhone(),
                userDetails.getAuthorities().iterator().next().getAuthority()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmergencyContactName(),
                signUpRequest.getEmergencyContactPhone(),
                signUpRequest.getSecurityQuestion(),
                signUpRequest.getSecurityAnswer() // Note: keeps answer in raw or normalized form
        );

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User with this email does not exist."));
        }

        User user = userOpt.get();
        Map<String, String> response = new HashMap<>();
        response.put("email", user.getEmail());
        response.put("securityQuestion", user.getSecurityQuestion());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User with this email does not exist."));
        }

        User user = userOpt.get();

        // Case insensitive security answer check
        if (!user.getSecurityAnswer().trim().equalsIgnoreCase(request.getSecurityAnswer().trim())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Security answer is incorrect."));
        }

        // Reset password
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Password has been reset successfully!"));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updateData) {
        Optional<User> userOpt = userRepository.findByEmail(updateData.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User not found."));
        }

        User user = userOpt.get();
        user.setName(updateData.getName());
        user.setEmergencyContactName(updateData.getEmergencyContactName());
        user.setEmergencyContactPhone(updateData.getEmergencyContactPhone());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Profile updated successfully!"));
    }
}
