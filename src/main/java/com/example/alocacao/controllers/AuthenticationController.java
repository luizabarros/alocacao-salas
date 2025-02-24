package com.example.alocacao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.alocacao.dtos.AuthenticationData;
import com.example.alocacao.dtos.TokenDTO;
import com.example.alocacao.entities.Professor;
import com.example.alocacao.services.JWTService;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService tokenService;

    @PostMapping
    public ResponseEntity<TokenDTO> login(@RequestBody AuthenticationData data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
        		data.login(),  
        		data.password()   
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        Professor professor = (Professor) authentication.getPrincipal();
        
        String jwt = tokenService.generateToken(professor.getEmail());
        boolean isAdmin = professor.getIsAdmin();

        return ResponseEntity.ok(new TokenDTO(jwt, isAdmin));
    }

}
