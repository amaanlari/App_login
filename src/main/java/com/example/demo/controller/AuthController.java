package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.JWTResponse;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;



@RestController
@RequestMapping("/api")
public class AuthController {
        private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody User user) throws AuthenticationException {
        Authentication authentication;
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if (authentication.isAuthenticated()) {
             logger.info("Authenticated and token is");
            String token=jwtUtil.generateToken(user.getUsername());
            JWTResponse response=JWTResponse.builder()
            .jwtToken(token).username(user.getUsername()).build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        } else {
            throw new RuntimeException("Invalid login credentials");
        }
    }
}
