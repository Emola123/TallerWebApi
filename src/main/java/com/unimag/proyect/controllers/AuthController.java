package com.unimag.proyect.controllers;


import com.unimag.proyect.dtos.LoginRequest;
import com.unimag.proyect.dtos.LoginResponse;
import com.unimag.proyect.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        final UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token);
    }
}
